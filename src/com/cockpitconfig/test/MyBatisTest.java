package com.cockpitconfig.test;


import static org.junit.Assert.*;

import java.io.Reader;
import java.util.ArrayList;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cockpitconfig.objects.Communication;

public class MyBatisTest {

 private static Logger log = LoggerFactory.getLogger(MyBatisTest.class);
 private static SqlSessionFactory sf;


 @BeforeClass
 public static void setUp() throws Exception {
  log.info("starting up myBatis tests");
  String resource = "Configuration.xml";
  Reader reader = Resources.getResourceAsReader(resource);
  System.out.println(reader);
  sf = new SqlSessionFactoryBuilder().build(reader,"testing"); //we're using the 'testing'
 }

 @AfterClass
 public static void tearDown() throws Exception {
  log.info("closing down myBatis tests");
 }

 @Test
 public void getAllMedium(){

  SqlSession session = sf.openSession();
  try
  {
  ArrayList<Communication> comm = (ArrayList<Communication>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getMediumByID", 1);
  assertNotNull("Medium list is null",comm);

  System.out.println(comm.get(0).getMedium());
  }
  finally
  {
   session.close();
  }
 }
}