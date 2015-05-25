package xyz.jadonfowler.phasebot;

import java.net.Proxy;

public class Bot {
	
	private String username;
	private String password;
	private String host;
	private int port;
	private Proxy proxy = Proxy.NO_PROXY;
	
	public double x = 0;
	public double y = 0;
	public double z = 0;
	public float pitch = 0;
	public float yaw = 0;
	public int entityId = 0;
	
	public Bot(String username, String password, String host, int port, Proxy proxy) {
		super();
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.proxy = proxy;
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
