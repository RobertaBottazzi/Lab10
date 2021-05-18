package it.polito.tdp.rivers.db;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

public class TestRiversDAO {

	public static void main(String[] args) {
		RiversDAO dao = new RiversDAO();
		//System.out.println(dao.getAllRivers());
		River river=null;
		for(River r: dao.getAllRivers()) {
			if(r.getId()==1)
				river=r;
		}
		List<Flow> flows=dao.getAllFlows(river);
		List<Flow> alcuni= new ArrayList<>();
		for(int i=0; i<10;i++)
			alcuni.add(flows.get(i));
			
		System.out.println(alcuni);
	}

}
