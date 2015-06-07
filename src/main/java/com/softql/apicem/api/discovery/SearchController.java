package com.softql.apicem.api.discovery;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
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
import com.softql.apicem.model.DeviceQuestionare;
import com.softql.apicem.model.DiscoveryDevices;
import com.softql.apicem.service.SearchService;

@RestController
@RequestMapping(value = Constants.URI_API + Constants.URI_DISCOVERY)
public class SearchController {

	private static final Logger log = LoggerFactory.getLogger(SearchController.class);

	@Inject
	private SearchService searchService;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<DiscoveryDevices>> getDevices(
			@RequestParam(value = "from", required = false) String fromIP,
			@RequestParam(value = "q", required = false) String keyword) {

		String from = null;
		String to = null;

		if (StringUtils.isNotBlank(keyword) && !StringUtils.equalsIgnoreCase("undefined", keyword)) {
			String[] strings = StringUtils.split(keyword, "-");
			from = strings[0];
			to = strings[1];

		}

		List<DiscoveryDevices> discoveryDevices = searchService.getDevices();

		if (log.isDebugEnabled()) {
			log.debug("get posts size @" + discoveryDevices.size());
		}

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
					deviceMap.put(platformId, discoveryDevice);
				} else {
					deviceMap.put(platformId, device);
				}
			}
		}
		return new ResponseEntity<>(deviceMap.values(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/questionare", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<DeviceQuestionare> getQuestionare(@PathVariable("id") String discoveryId) {
		if (log.isDebugEnabled()) {
			log.debug("get all posts of q@" + discoveryId);
		}

		DeviceQuestionare deviceQuestionare = searchService.getQuestionare(discoveryId);

		if (log.isDebugEnabled()) {
			log.debug("get posts size @" + deviceQuestionare);
		}

		return new ResponseEntity<>(deviceQuestionare, HttpStatus.OK);
	}
}
