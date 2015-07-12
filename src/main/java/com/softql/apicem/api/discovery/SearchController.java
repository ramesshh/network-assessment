package com.softql.apicem.api.discovery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softql.apicem.Constants;
import com.softql.apicem.model.DiscoveryDevices;
import com.softql.apicem.service.ApicEmService;
import com.softql.apicem.util.ApicemUtils;
import com.softql.apicem.util.URLUtil;

@RestController
@RequestMapping(value = Constants.URI_API + Constants.URI_DISCOVERY)
public class SearchController {

	private static final Logger log = LoggerFactory.getLogger(SearchController.class);

	@Inject
	private ApicEmService apicEmService;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<DiscoveryDevices>> getDevices(
			@RequestParam(value = "from", required = false) String fromIP,
			@RequestParam(value = "q", required = false) String keyword, HttpServletRequest request) {

		List<DiscoveryDevices> discoveryDevices = new ArrayList<DiscoveryDevices>();

		String url = URLUtil.constructUrl(request.getHeader("apicem"), null, request.getHeader("version"),
				"/network-device/1/10000000", request.getHeader("X-Access-Token"));
		try {
			discoveryDevices = apicEmService.getDevices(url);
		} catch (Throwable e) {
			e.printStackTrace();
			return new ResponseEntity<>(discoveryDevices, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String tagurl = URLUtil.constructUrl(request.getHeader("apicem"), null, request.getHeader("version"),
				"/network-device/tag", request.getHeader("X-Access-Token"));

		Map<String, String> tags = apicEmService.getTags(tagurl);

		for (DiscoveryDevices d : discoveryDevices) {
			d.setTags(tags.get(d.getId()));
		}

		log.debug("get posts size {}", discoveryDevices.size());

		return new ResponseEntity<>(discoveryDevices, HttpStatus.OK);
	}

	@RequestMapping(value = "{groupType}/groupby", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Collection<DiscoveryDevices>> groupByData(@PathVariable("groupType") String groupType,
			@RequestBody final List<DiscoveryDevices> deviceList) {

		Map<String, DiscoveryDevices> deviceMap = new HashMap<String, DiscoveryDevices>();

		if (!CollectionUtils.isEmpty(deviceList)) {
			for (DiscoveryDevices device : deviceList) {
				String platformId = device.getPlatformId();
				DiscoveryDevices thinDevice = new DiscoveryDevices();
				thinDevice.setPlatformId(platformId);
				thinDevice.setQty(1);
				thinDevice.setLocationName(device.getLocationName());
				thinDevice.setTags(device.getTags());
				thinDevice.setType(device.getType());

				if (deviceMap.containsKey(platformId)) {
					DiscoveryDevices discoveryDevice = deviceMap.get(platformId);
					int qty = discoveryDevice.getQty() + 1;
					thinDevice.setQty(qty);
					thinDevice.setLocationName(ApicemUtils.join(',', discoveryDevice.getLocationName(),
							device.getLocationName()));
					thinDevice.setTags(ApicemUtils.join(',', discoveryDevice.getTags(), device.getTags()));
					deviceMap.put(platformId, thinDevice);
				} else {
					deviceMap.put(platformId, thinDevice);
				}
			}
		}
		return new ResponseEntity<>(deviceMap.values(), HttpStatus.OK);
	}

	@RequestMapping(value = "export", method = RequestMethod.POST)
	@ResponseBody
	public void export(@RequestBody final List<DiscoveryDevices> deviceList, HttpServletResponse response) {

		try {

			InputStream resourceAsStream = this.getClass().getClassLoader()
					.getResourceAsStream("Network_Assessment_app.xlsx");

			XSSFWorkbook workbook = new XSSFWorkbook(resourceAsStream);
			XSSFSheet sheet = workbook.getSheetAt(0);

			int router = 0;
			int switches = 0;
			int wireless = 0;

			int startRow = 6;
			for (DiscoveryDevices devices : deviceList) {
				XSSFRow row = sheet.createRow(startRow);
				row.createCell(0).setCellValue(devices.getPlatformId());
				row.createCell(1).setCellValue(devices.getType());
				row.createCell(2).setCellValue(devices.getSoftwareVersion());
				row.createCell(3).setCellValue(devices.getLocationName());
				row.createCell(4).setCellValue(devices.getTags());
				row.createCell(5).setCellValue(devices.getFamily());
				row.createCell(6).setCellValue(devices.getVendor());
				row.createCell(7).setCellValue(devices.getHostname());
				row.createCell(8).setCellValue(devices.getSerialNumber());
				row.createCell(9).setCellValue(devices.getManagementIpAddress());
				row.createCell(10).setCellValue(devices.getMacAddress());
				row.createCell(11).setCellValue(devices.getReachabilityStatus());
				startRow++;
				if (StringUtils.equalsIgnoreCase(devices.getType(), "ROUTER")) {
					router++;
				} else if (StringUtils.equalsIgnoreCase(devices.getType(), "SWITCH")) {
					switches++;
				} else if (StringUtils.equalsIgnoreCase(devices.getType(), "WIRELESS")) {
					wireless++;
				}
			}

			String headerValue = sheet.getRow(3).getCell(0).getStringCellValue();
			headerValue = StringUtils.replace(headerValue, "ALL", String.valueOf(router + switches + wireless));
			headerValue = StringUtils.replace(headerValue, "RUT", String.valueOf(router));
			headerValue = StringUtils.replace(headerValue, "SWT", String.valueOf(switches));
			headerValue = StringUtils.replace(headerValue, "WRL", String.valueOf(wireless));
			sheet.getRow(3).getCell(0).setCellValue(headerValue);

			// create a temp file
			File temp = File.createTempFile("Network_Assessment_App", ".xlsx");
			FileOutputStream fileOut = new FileOutputStream(temp.getAbsolutePath());
			workbook.write(fileOut);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			workbook.write(byteArrayOutputStream);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=Network_Assessment_App.xlsx");
			response.setContentLength(byteArrayOutputStream.toByteArray().length);
			ServletOutputStream out = response.getOutputStream();
			out.write(byteArrayOutputStream.toByteArray());
			out.flush();
			out.close();

			fileOut.close();
			byteArrayOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
