package xyz.jadonfowler.phasebot;

import java.net.Proxy;

import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSwingArmPacket;
import org.spacehq.packetlib.Session;

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

	boolean isDerp = false;

	public Vector3d[] positions;

	public Bot(String username, String password, String host, int port, Proxy proxy) {
		super();
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.proxy = proxy;
		this.positions = new Vector3d[4]; // TODO May need more
		this.pos = new Vector3d(0, 0, 0);
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

	public void derp(final Session s) {
		new Thread(new Runnable() {
			public void run() {
				while (isDerp) {
					look(s, (PhaseBot.random.nextFloat() * 10000) % 180,
							((PhaseBot.random.nextFloat() * 10000) % 180));
					swing(s);
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}

	public void look(final Session s, float yaw, float pitch) {
		s.send(new ClientPlayerPositionRotationPacket(false, pos.x, pos.y, pos.z, yaw, pitch));
	}

	public void swing(Session s) {
		s.send(new ClientSwingArmPacket());
	}

}
