package com.softql.apicem.api.discovery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
				if (deviceMap.containsKey(platformId)) {
					DiscoveryDevices discoveryDevice = deviceMap.get(platformId);
					int qty = discoveryDevice.getQty() + 1;
					discoveryDevice.setQty(qty);
					discoveryDevice.setLocationName(ApicemUtils.join(',', discoveryDevice.getLocationName(),
							device.getLocationName()));
					discoveryDevice.setTags(ApicemUtils.join(',', discoveryDevice.getTags(), device.getTags()));

					deviceMap.put(platformId, discoveryDevice);
				} else {
					deviceMap.put(platformId, device);
				}
			}
		}
		return new ResponseEntity<>(deviceMap.values(), HttpStatus.OK);
	}
}
