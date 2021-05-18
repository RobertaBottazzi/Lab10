package it.polito.tdp.rivers.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Model model= new Model();
		River river=null;
		for(River r: model.getRivers()) {
			if(r.getId()==2)
				river=r;
		}
		List<Flow> flows=model.getFlowsPerRiver(river);
		System.out.println("Fmed: "+model.getMedia(flows));
		model.init(flows, 20);
		model.run();
		System.out.println("GIORNI: "+model.getGiorniSenzaIrrigazione());
		System.out.println("CMedia: "+model.getCMedia());

	}

}
