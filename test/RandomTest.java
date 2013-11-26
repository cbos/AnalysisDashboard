import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

import java.util.List;

import model.analysis.TestMethod;

import org.junit.Test;

import play.db.jpa.JPA;

public class RandomTest
{

	@Test
	public void findById()
	{
		running(fakeApplication(), new Runnable()
		{
			@Override
			public void run()
			{
				JPA.withTransaction(new play.libs.F.Callback0()
				{
					@Override
					public void invoke()
					{
						//String genericQueryPart = "from mob_abon a, IN(a.user) u, IN(u.klant)k where k.klant_id= :klantid and a.inuse=1";

						//						String genericQueryPart = "select DISTINCT t from testmethod t, testfailure f where f.testMethod = t and f.randomFailure=1 order by f.id DESC";
						//	
						//						Query dataQuery = JPA.em().createQuery(genericQueryPart);
						//						//dataQuery.setMaxResults(50);
						//	
						////						List resultList = dataQuery.getResultList();
						////						
						////						for (Object object : resultList) {
						////							
						////						}
						//	
						//						List<TestMethod> resultList = dataQuery.getResultList();
						//						
						//						for (TestMethod object : resultList) {
						//							System.out.format("TestMethod methodId %s methodName %s className %s\n", object.getId(), object.getMethodName(), object.getTestClass().getClassName());
						//						}

						System.out.println(TestMethod.getTotalRandomFailures());

						List<TestMethod> randomFailures = TestMethod.getRandomFailures(1, 10);
						System.out.println(randomFailures.size());

						for (TestMethod object : randomFailures)
						{
							System.out.format("TestMethod methodId %s methodName %s className %s\n",
																object.getId(),
																object.getMethodName(),
																object.getTestClass().getClassName());
						}
					}
				});
			}
		});
	}
}
