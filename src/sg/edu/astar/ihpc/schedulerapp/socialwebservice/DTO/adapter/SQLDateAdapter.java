package sg.edu.astar.ihpc.schedulerapp.socialwebservice.DTO.adapter;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SQLDateAdapter extends XmlAdapter<Date, java.sql.Date>{

	@Override
	public Date marshal(java.sql.Date v) throws Exception {
		return new Date(v.getTime());
	}

	@Override
	public java.sql.Date unmarshal(Date v) throws Exception {
		return new java.sql.Date(v.getTime());
	}

}
