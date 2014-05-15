package me.mini.tests;

import me.mini.cassandra.client.CassandraClient;
import me.mini.entity.UrlEntity;
import me.mini.utils.MiniMeException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Cassandra Client
 * 
 * @author parampreetsethi
 * 
 */
public class CassandraClientTest {
	CassandraClient client;

	String testKey = "abcedfg";
	String testUrl = "http://test123.com/abc/abc.html";

	@Before
	public void prepare() throws MiniMeException {
		// Intitalize connection
		client = CassandraClient.getInstance();

		// Clean up the data
		client.deleteQueryExecute(testKey);
	}

	public void writeValidData() {

	}

	@Test
	public void writeQueryExecuteNullInsertTest() throws MiniMeException {
		// Insert data into Database
		UrlEntity entity = null;
		boolean isInserted = client.writeQueryExecute(entity);

		// Verify nothing inserted in database
		Assert.assertFalse(isInserted);
	}

	@Test
	public void writeQueryExecuteValidInsertTest() throws MiniMeException {
		// Insert data into Database
		UrlEntity entity = new UrlEntity();
		entity.setOrigUrl(testUrl);

		entity.setUrlHash(testKey);
		boolean isInserted = client.writeQueryExecute(entity);

		// Verify inserted in database
		Assert.assertTrue(isInserted);
	}
	
	@After
	public void tear() throws MiniMeException {
		// clean up data
		client.deleteQueryExecute(testKey);
	}

}
