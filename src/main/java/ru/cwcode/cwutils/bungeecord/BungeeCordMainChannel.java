package ru.cwcode.cwutils.bungeecord;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public final class BungeeCordMainChannel {
  public static final String CHANNEL = "BungeeCord";
  private static final long DEFAULT_TIMEOUT_MS = 5000;
  
  private static final Map<String, Deque<PendingRequest<?>>> PENDING = new ConcurrentHashMap<>();
  private static final Map<String, BiConsumer<ByteArrayDataInput, Player>> HANDLERS = new HashMap<>();
  private static volatile boolean registered = false;
  
  static {
    HANDLERS.put("IP", (in, p) -> complete("IP", p.getName(), new PlayerIp(in.readUTF(), in.readInt())));
    HANDLERS.put("IPOther", (in, p) -> {
      String name = in.readUTF();
      complete("IPOther", name, new PlayerIpOther(name, in.readUTF(), in.readInt()));
    });
    HANDLERS.put("PlayerCount", (in, p) -> {
      String server = in.readUTF();
      complete("PlayerCount", server, new PlayerCount(server, in.readInt()));
    });
    HANDLERS.put("PlayerList", (in, p) -> {
      String server = in.readUTF();
      String csv = in.readUTF();
      List<String> players = csv.isEmpty() ? List.of() : Arrays.asList(csv.split(", "));
      complete("PlayerList", server, new PlayerListResult(server, players));
    });
    HANDLERS.put("GetPlayerServer", (in, p) -> {
      String name = in.readUTF();
      complete("GetPlayerServer", name, new PlayerServer(name, in.readUTF()));
    });
    HANDLERS.put("GetServers", (in, p) -> {
      String csv = in.readUTF();
      List<String> servers = csv.isEmpty() ? List.of() : Arrays.asList(csv.split(", "));
      complete("GetServers", null, servers);
    });
    HANDLERS.put("GetServer", (in, p) ->
      complete("GetServer", p.getName(), in.readUTF())
    );
    HANDLERS.put("UUID", (in, p) ->
      complete("UUID", p.getName(), in.readUTF())
    );
    HANDLERS.put("UUIDOther", (in, p) -> {
      String name = in.readUTF();
      complete("UUIDOther", name, new PlayerUuid(name, in.readUTF()));
    });
    HANDLERS.put("ServerIP", (in, p) -> {
      String name = in.readUTF();
      complete("ServerIP", name, new ServerIp(name, in.readUTF(), in.readUnsignedShort()));
    });
  }
  
  private BungeeCordMainChannel() {
  }
  /**
   * Перемещает указанного игрока на другой сервер.
   */
  public static void connect(Plugin pl, Player p, String server) {
    sendCmd(pl, p, "Connect", server);
  }
  
  /**
   * Перемещает другого игрока на другой сервер.
   */
  public static void connectOther(Plugin pl, Player p, String playerName, String server) {
    sendCmd(pl, p, "ConnectOther", playerName, server);
  }
  
  /**
   * Отправляет чат-сообщение целевому игроку или всем (ALL) игрокам.
   */
  public static void message(Plugin pl, Player p, String target, String msg) {
    sendCmd(pl, p, "Message", target, msg);
  }
  
  /**
   * Отправляет "сырое" JSON-сообщение целевому игроку или всем (ALL) игрокам.
   */
  public static void messageRaw(Plugin pl, Player p, String target, String json) {
    sendCmd(pl, p, "MessageRaw", target, json);
  }
  
  /**
   * Кикает игрока с указанной причиной.
   */
  public static void kickPlayer(Plugin pl, Player p, String target, String reason) {
    sendCmd(pl, p, "KickPlayer", target, reason);
  }
  
  /**
   * Кикает игрока с JSON-сообщением.
   */
  public static void kickPlayerRaw(Plugin pl, Player p, String target, String json) {
    sendCmd(pl, p, "KickPlayerRaw", target, json);
  }
  
  /**
   * Запрашивает IP-адрес и порт текущего игрока.
   */
  public static CompletableFuture<PlayerIp> ip(Plugin pl, Player p) {
    return sendRequest(pl, p, "IP", p.getName());
  }
  
  /**
   * Запрашивает IP-адрес и порт другого игрока.
   */
  public static CompletableFuture<PlayerIpOther> ipOther(Plugin pl, Player p, String other) {
    return sendRequest(pl, p, "IPOther", other, other);
  }
  
  /**
   * Запрашивает количество игроков на указанном сервере или во всей сети (ALL).
   */
  public static CompletableFuture<PlayerCount> playerCount(Plugin pl, Player p, String server) {
    return sendRequest(pl, p, "PlayerCount", server, server);
  }
  
  /**
   * Запрашивает список игроков на указанном сервере или во всей сети (ALL).
   */
  public static CompletableFuture<PlayerListResult> playerList(Plugin pl, Player p, String server) {
    return sendRequest(pl, p, "PlayerList", server, server);
  }
  
  /**
   * Запрашивает сервер, на котором находится указанный игрок.
   */
  public static CompletableFuture<PlayerServer> getPlayerServer(Plugin pl, Player p, String name) {
    return sendRequest(pl, p, "GetPlayerServer", name, name);
  }
  
  /**
   * Запрашивает список всех серверов в сети.
   */
  public static CompletableFuture<List<String>> getServers(Plugin pl, Player p) {
    return sendRequest(pl, p, "GetServers", null);
  }
  
  /**
   * Запрашивает имя текущего сервера.
   */
  public static CompletableFuture<String> getServer(Plugin pl, Player p) {
    return sendRequest(pl, p, "GetServer", p.getName());
  }
  
  /**
   * Запрашивает UUID текущего игрока.
   */
  public static CompletableFuture<String> uuid(Plugin pl, Player p) {
    return sendRequest(pl, p, "UUID", p.getName());
  }
  
  /**
   * Запрашивает UUID другого игрока.
   */
  public static CompletableFuture<PlayerUuid> uuidOther(Plugin pl, Player p, String name) {
    return sendRequest(pl, p, "UUIDOther", name, name);
  }
  
  /**
   * Запрашивает IP-адрес и порт сервера по его имени.
   */
  public static CompletableFuture<ServerIp> serverIp(Plugin pl, Player p, String server) {
    return sendRequest(pl, p, "ServerIP", server, server);
  }
  
  /**
   * Отправляет произвольное сообщение на указанный сервер или все (ALL) сервера.
   */
  public static void forward(Plugin pl, Player p, String target, String subchannel, byte[] payload) {
    sendCmd(pl, p, "Forward", target, subchannel, (short) payload.length, payload);
  }
  
  /**
   * Отправляет произвольное сообщение конкретному игроку.
   */
  public static void forwardToPlayer(Plugin pl, Player p, String target, String subchannel, byte[] payload) {
    sendCmd(pl, p, "ForwardToPlayer", target, subchannel, (short) payload.length, payload);
  }
  
  /**
   * Обрабатывает входящее сообщение с канала BungeeCord.
   */
  public static void handleIncoming(String channel, Player player, byte[] data) {
    if (!CHANNEL.equals(channel)) return;
    ByteArrayDataInput in = ByteStreams.newDataInput(data);
    String sub = in.readUTF();
    HANDLERS.getOrDefault(sub, (i, p) -> {}).accept(in, player);
  }
  
  private static void sendCmd(Plugin pl, Player carrier, Object... parts) {
    ensureRegistered(pl);
    Player c = resolveCarrier(carrier);
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    for (Object part : parts) writePart(out, part);
    c.sendPluginMessage(pl, CHANNEL, out.toByteArray());
  }
  
  private static <T> CompletableFuture<T> sendRequest(Plugin pl, Player carrier, String cmd, String key, Object... parts) {
    CompletableFuture<T> fut = await(cmd, key);
    sendCmd(pl, carrier, merge(new Object[]{cmd}, parts));
    return fut;
  }
  
  private static void writePart(ByteArrayDataOutput out, Object p) {
    if (p instanceof String s) out.writeUTF(s);
    else if (p instanceof Short s) out.writeShort(s);
    else if (p instanceof byte[] b) out.write(b);
  }
  
  private static Object[] merge(Object[] a, Object[] b) {
    Object[] r = Arrays.copyOf(a, a.length + b.length);
    System.arraycopy(b, 0, r, a.length, b.length);
    return r;
  }
  
  private static void ensureRegistered(Plugin plugin) {
    if (registered) return;
    registered = true;
    Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL);
    Bukkit.getMessenger().registerIncomingPluginChannel(plugin, CHANNEL, BungeeCordMainChannel::handleIncoming);
  }
  
  private static Player resolveCarrier(Player preferred) {
    if (preferred != null && preferred.isOnline()) return preferred;
    Player any = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
    if (any == null) throw new IllegalStateException("Нет онлайн-игроков для отправки plugin message");
    return any;
  }
  
  private static <T> CompletableFuture<T> await(String sub, String key) {
    PendingRequest<T> pr = new PendingRequest<>(key);
    PENDING.computeIfAbsent(sub, k -> new ConcurrentLinkedDeque<>()).add(pr);
    pr.future.orTimeout(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
             .whenComplete((r, e) -> PENDING.getOrDefault(sub, new ConcurrentLinkedDeque<>()).remove(pr));
    return pr.future;
  }
  
  @SuppressWarnings("unchecked")
  private static <T> void complete(String sub, String key, T val) {
    Deque<PendingRequest<?>> q = PENDING.get(sub);
    if (q == null) return;
    var it = q.iterator();
    while (it.hasNext()) {
      PendingRequest<?> pr = it.next();
      if (Objects.equals(pr.correlationKey, key) || (key == null && pr.correlationKey == null)) {
        it.remove();
        ((PendingRequest<T>) pr).future.complete(val);
        return;
      }
    }
  }
  
  public record PlayerIp(String ip, int port) {}
  
  public record PlayerIpOther(String userName, String address, int port) {}
  
  public record PlayerCount(String server, int count) {}
  
  public record PlayerListResult(String server, List<String> players) {}
  
  public record PlayerServer(String userName, String serverName) {}
  
  public record PlayerUuid(String userName, String uuid) {}
  
  public record ServerIp(String serverName, String ip, int port) {}
  
  private static final class PendingRequest<T> {
    final String correlationKey;
    final CompletableFuture<T> future = new CompletableFuture<>();
    
    PendingRequest(String key) {
      this.correlationKey = key;
    }
  }
}
