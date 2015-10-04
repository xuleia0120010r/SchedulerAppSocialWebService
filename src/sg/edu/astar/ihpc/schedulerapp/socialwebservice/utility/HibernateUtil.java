package sg.edu.astar.ihpc.schedulerapp.socialwebservice.utility;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static SessionFactory sf;
	
	static {
		Configuration conf = new Configuration();
		// load hibernate configuration
		conf.configure("/hibernate.cfg.xml");
		// get sessionFactory
		sf = conf.buildSessionFactory();
	}

	public static Session getSession() {
		// get session
		Session session = sf.openSession();
		return session;
	}
	
}
