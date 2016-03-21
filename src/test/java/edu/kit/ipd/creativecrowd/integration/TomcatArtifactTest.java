package edu.kit.ipd.creativecrowd.integration;

import static org.junit.Assert.*;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Before;
import org.junit.Test;

public class TomcatArtifactTest {
	@Test
	public void test() throws MavenInvocationException, IOException, InterruptedException {
		if (System.getProperty("maven.home") == null || System.getProperty("maven.home").contains("EMBEDDED")) {
			System.out.println("Not in non-embedded maven context; skipping maven integration test");
			return;
		}
		
		Thread serverThread = new Thread() {
			public void run() {
				InvocationRequest request = new DefaultInvocationRequest();
				Properties props = new Properties();
				request.setProperties(props);

				// Configure the runner to use the current maven repo
				request.setPomFile( new File( "./pom.xml" ) );

				request.setGoals( Collections.singletonList( "tomcat7:run" ) );
				Invoker invoker = new DefaultInvoker();
				invoker.setWorkingDirectory(new File("."));
				InvocationResult res = null;
				try {
					res = invoker.execute( request );
				} catch (Exception e) {
					fail(e.getMessage());
				}

				assertEquals(0, res.getExitCode());
			}
		};

		serverThread.start();
		int tries = 0; 
		while(true) {
			try {
				SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9090));
				ByteBuffer b = ByteBuffer.wrap("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
				socketChannel.write(b);
				ByteBuffer buf = ByteBuffer.allocate(1024);
				int bytesRead = socketChannel.read(buf);
				String content = new String(buf.array());

				// shut down the server...
				socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9090));
				b = ByteBuffer.wrap("GET /exit HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
				socketChannel.write(b);

				assertTrue(content.indexOf("hallo welt") > -1);
				
				break;
			} catch(IOException e) {
				if(tries++ > 30) {
					serverThread.interrupt();
					fail("Timed out waiting for Tomcat to boot.");
				}
				Thread.sleep(500);
			}
		}	
	}
}
