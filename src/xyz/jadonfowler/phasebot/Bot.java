package xyz.jadonfowler.phasebot;

import java.net.Proxy;

import xyz.jadonfowler.phasebot.util.Vector3d;

public class Bot {
	
	String username;
	String password;
	String host;
	int port;
	Proxy proxy = Proxy.NO_PROXY;
	
	public Vector3d pos;
	public float pitch = 0;
	public float yaw = 0;
	public int entityId = 0;
	
	public Vector3d[] positions;
	
	public Bot(String username, String password, String host, int port, Proxy proxy) {
		super();
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.proxy = proxy;
		this.positions = new Vector3d[4]; //TODO May need more
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}
	
}
