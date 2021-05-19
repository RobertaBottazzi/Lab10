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
		System.out.println("Fmed: "+model.getMedia(model.getFlowsPerRiver(river))*3600*24);
		model.init(model.getFlowsPerRiver(river), 0.5);
		model.run();
		System.out.println("GIORNI: "+model.getGiorniSenzaIrrigazione());
		System.out.println("GIORNI TOTALI: "+model.getFlowsPerRiver(river).size());
		System.out.println("CMedia: "+model.getCMedia(model.getFlowsPerRiver(river)));

	}

}
