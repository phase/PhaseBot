package xyz.jadonfowler.phasebot;

import java.net.Proxy;

import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.Face;
import org.spacehq.mc.protocol.data.game.values.entity.player.PlayerAction;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSwingArmPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.block.Block;
import xyz.jadonfowler.phasebot.block.Material;
import xyz.jadonfowler.phasebot.entity.Entity;
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
	public boolean isDerp = false;
	private Client client;

	public Vector3d[] positions;

	public Chunk[][][] chunks = new Chunk[512][512][512];

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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void derp(final Session s) {
		new Thread(new Runnable() {
			public void run() {
				while (isDerp) {
					look((PhaseBot.random.nextFloat() * 10000) % 180, ((PhaseBot.random.nextFloat() * 10000) % 180));
					swing();
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}

	public void look(float yaw, float pitch) {
		client.getSession().send(new ClientPlayerPositionRotationPacket(false, pos.x, pos.y, pos.z, yaw, pitch));
	}

	public void swing() {
		client.getSession().send(new ClientSwingArmPacket());
	}

	public void moveTo(Entity e) {
		moveTo((int) Math.floor(e.x), (int) Math.floor(e.y), (int) Math.floor(e.z));
	}

	public void moveTo(int ax, int ay, int az) {
		move(ax - pos.x, ay - pos.y, az - pos.z);
	}

	public void move(double rx, double ry, double rz) {
		System.out.println("m: " + rx + " " + ry + " " + rz);
		double l = (pos.x + rx) - pos.x;
		double w = (pos.z + rz) - pos.z;
		double c = Math.sqrt(l * l + w * w);
		double a1 = -Math.asin(l / c) / Math.PI * 180;
		double a2 = Math.acos(w / c) / Math.PI * 180;
		if (a2 > 90)
			yaw = (float) (180 - a1);
		else
			yaw = (float) a1;

		pitch = 0; //pitch is waaay too hard

		System.out.println("p: " + pitch + " y:" + yaw);
		int numberOfSteps = (int) ((int) 2.0 * Math
				.floor(Math.sqrt(Math.pow(rx, 2) + Math.pow(ry, 2) + Math.pow(rz, 2))));
		double sLx = rx / numberOfSteps;
		double sLy = ry / numberOfSteps;
		double sLz = rz / numberOfSteps;
		System.out.println("s: " + sLx + " " + sLy + " " + sLz + " : " + numberOfSteps);
		for (int i = 0; i < numberOfSteps; i++) {
			client.getSession().send(
					new ClientPlayerPositionRotationPacket(false, sLx + pos.x, sLy + pos.y, sLz + pos.z, yaw, pitch));
			pos.x = sLx + pos.x;
			pos.y = sLy + pos.y;
			pos.z = sLz + pos.z;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		client.getSession().send(
				new ClientPlayerPositionRotationPacket(false, ((int) pos.x) + 0.5d, ((int) pos.y),
						((int) pos.z) + 0.5d, yaw, pitch));
	}

	public void breakBlock(int rx, int ry, int rz) {
		final Position p = new Position((int) Math.floor(pos.x + rx), (int) Math.floor(pos.y + ry),
				(int) Math.floor(pos.z + rz));
		System.out.println("Digging at: " + p.getX() + " " + p.getY() + " " + p.getZ());
		client.getSession().send(new ClientPlayerActionPacket(PlayerAction.START_DIGGING, p, Face.TOP));
		swing();
		client.getSession().send(new ClientPlayerActionPacket(PlayerAction.FINISH_DIGGING, p, Face.TOP));
	}

	public void say(String s) {
		client.getSession().send(new ClientChatPacket(s));
	}
	
	public Vector3d relativeToAbsolute(Vector3d d){
		return new Vector3d(pos.x+d.x, pos.y+d.y, pos.z+d.z);
	}
	
	public Face getPlaceFace(Vector3d d) {
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				for (int z = -1; z < 2; z++) {
					Block b = new Block(new Vector3d(x+d.x, y+d.y, z+d.z));
					if (b.getMaterial() != Material.AIR) {
						if ((x == -1 && z == 0 && y == 0))
							return Face.WEST;
						else if ((x == 1 && z == 0 && y == 0))
							return Face.EAST;
						else if ((x == 0 && z == 0 && y == 1))
							return Face.BOTTOM;
						else if ((x == 0 && z == 0 && y == -1))
							return Face.TOP;
						else if ((x == 0 && z == 1 && y == 0))
							return Face.SOUTH;
						else if ((x == 0 && z == -1 && y == 0))
							return Face.NORTH;
					}
				}
			}
		}
		return Face.INVALID;
	}
}
