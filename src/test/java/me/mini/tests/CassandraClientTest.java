package me.mini.tests;

import me.mini.bean.UrlMapping;
import me.mini.cassandra.query.CassandraUrlQueryUtil;
import me.mini.utils.MinimeException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for Cassandra Client
 * 
 * @author parampreetsethi
 * 
 *         Tests in this class are disabled for now. These should be enabled
 *         once the Cassandra setup is ready and the tests will verify if the
 *         environment is correctly setup or not.
 * 
 */
public class CassandraClientTest {
	CassandraUrlQueryUtil client;

	String testKey = "abcedfg";
	String testUrl = "http://test123.com/abc/abc.html";

	@Before
	public void prepare() throws MinimeException {
		// Intitalize connection
		client = CassandraUrlQueryUtil.getInstance();

		// Clean up the data
		client.deleteQueryExecute(testKey);
	}

	@Test
	@Ignore
	public void writeQueryExecuteNullInsertTest() throws MinimeException {
		// Insert data into Database
		UrlMapping entity = null;
		boolean isInserted = client.writeQuery(entity);

		// Verify nothing inserted in database
		Assert.assertFalse(isInserted);
	}

	@Test
	@Ignore
	public void writeQueryExecuteValidInsertTest() throws MinimeException {
		// Insert data into Database
		UrlMapping entity = new UrlMapping();
		entity.setOrigUrl(testUrl);

		entity.setUrlHash(testKey);
		boolean isInserted = client.writeQuery(entity);

		// Verify inserted in database
		Assert.assertTrue(isInserted);
	}

	@After
	public void tear() throws MinimeException {
		// clean up data
		client.deleteQueryExecute(testKey);
	}

}
