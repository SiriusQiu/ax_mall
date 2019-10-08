package org.linlinjava.ax.wx.service;

import org.linlinjava.ax.db.domain.AxRegion;
import org.linlinjava.ax.db.service.AxRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhy
 * @date 2019-01-17 23:07
 **/
@Component
public class GetRegionService {

	@Autowired
	private AxRegionService regionService;

	private static List<AxRegion> axRegions;

	protected List<AxRegion> getAxRegions() {
		if(axRegions==null){
			createRegion();
		}
		return axRegions;
	}

	private synchronized void createRegion(){
		if (axRegions == null) {
			axRegions = regionService.getAll();
		}
	}
}
