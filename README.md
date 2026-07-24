# TkachUtils 📦

> Коллекция готовых решений для Paper/Spigot/Velocosity плагинов

Библиотека утилит для упрощения разработки Minecraft-плагинов. Включает в себя анимации, коллекции, работу с предметами,
планировщик и многое другое.

---

## 📖 Содержание

- [🎬 Анимации и визуал](#-анимации-и-визуал)
- [📦 Коллекции](#-коллекции)
- [🛠️ Предметы и инвентари](#-предметы-и-инвентари)
- [⏱️ Время и даты](#-время-и-даты)
- [🔤 Текст](#-текст)
- [📊 Утилиты](#-утилиты)
- [📚 Полный список утилит](#-полный-список-утилит)

---

## 🎬 Анимации и визуал

### SimpleAnimation / ObjectAnimation

Плавная анимация значений с функциями плавности (ease, easeIn, easeOut, cubic bezier и др.)

**Пример — анимация босс-бара:**

```java
new SimpleAnimation()
  .

setProperties(new AnimationProperties(0, 100,1)  // от 0 до 100, 1 тик на кадр
      .

setTimingFunction(TimingFunctions.easeInOut))  // плавное начало и конец
  .

setAction(progress ->bossBar.

progress(progress /100.0))
  .

before(() ->{
  bossBar.

progress(0);
      bossBar.

visible(true);
  })
    .

after(() ->plugin.

getLogger().

info("Анимация завершена!"))
  .

andBack()  // проиграть назад после завершения
  .

start(plugin, ExecutionMode.SYNC);
```

**Пример — анимация объекта:**

```java
ObjectAnimation<Location> anim = ObjectAnimation.before(() -> startLocation)
  .setProperties(new AnimationProperties(0, 60, Duration.ofSeconds(2)))
  .setAction((progress, loc) -> {
      // progress: 0.0 .. 1.0
      Location interpolated = startLocation.clone().lerp(endLocation, progress);
      armorStand.teleport(interpolated);
  })
  .after(finalLoc -> finalLoc.getWorld().playEffect(finalLoc, Effect.ENDER_DRAGON_BREATH, 0));

anim.start(plugin, ExecutionMode.SYNC);
```

**Доступные timing-функции:**

- `TimingFunctions.linear` — линейная
- `TimingFunctions.ease` — стандартная
- `TimingFunctions.easeIn` / `easeOut` / `easeInOut`
- `CubicBezier.ofCubicBezier(p0, p1, p2, p3)` — кастомная кривая Безье

---

### Hologram

Быстрое создание временных текстовых голограмм:

```java
// Показать текст на 5 секунд (100 тиков)
Hologram.showText(
  Component.text("✨ Добро пожаловать на сервер!").

color(NamedTextColor.GOLD),
    100,
location,
plugin
);

// Чтобы голограмма осталась навсегда, передайте 0
  Hologram.

showText(Component.text("🏆spawn"), 0,spawnLocation,plugin);
```

---

### PersonalBossBar / BroadcastBossBar

**PersonalBossBar** — персональные босс-бары для каждого игрока с динамическим обновлением:

```java
// 1. Создаём босс-бар через билдер
PersonalBossBar healthBar = PersonalBossBar.builder()
    .title(player -> Component.text("HP: " + (int) player.getHealth())
        .color(NamedTextColor.RED))
    .progress(player -> (float) (player.getHealth() / player.getMaxHealth()))
    .color(player -> player.getHealth() < 5 ? BossBar.Color.RED : BossBar.Color.GREEN)
    .overlay(BossBar.Overlay.NOTCHED_12)
    .shouldDisplay(player -> player.hasPermission("ui.healthbar"))  // показывать только с пермишеном
    .shouldRemove(() -> false)  // не удалять автоматически
    .build();

// 2. Создаём менеджер и регистрируем босс-бар
PersonalBossBarManager manager = new PersonalBossBarManager(plugin, false, 1);  // async=false, delay=1 тик
manager.add(healthBar);

// Менеджер автоматически обновляет босс-бар всем игрокам каждый тик
// и убирает босс-бар при выходе игрока
```

**BroadcastBossBar** — один босс-бар для всех игроков:

```java
BroadcastBossBar serverBar = BroadcastBossBar.builder()
                                             .title(() -> Component.text("⏳ Перезапуск через: " + getRestartTime()))
                                             .progress(() -> getRestartProgress())  // 0.0 .. 1.0
                                             .color(BossBar.Color.YELLOW)
                                             .build();

BroadcastBossBarManager manager = new BroadcastBossBarManager(plugin, false, 20);  // обновлять раз в секунду
manager.

add(serverBar);

// Обновить вручную
serverBar.

update();
```

---

## 📦 Коллекции

### ExpiredSet

Set с автоматическим истечением элементов по времени — идеально для cooldown'ов:

```java
ExpiredSet<UUID> cooldowns = new ExpiredSet<>();

// Установить cooldown на 30 секунд
public void startCooldown(Player player) {
    cooldowns.setExpired(player.getUniqueId(), Duration.ofSeconds(30));
    useAbility(player);
}

// Проверить готовность
public boolean isReady(Player player) {
    return !cooldowns.isActive(player.getUniqueId());
}

// Получить процент выполнения cooldown
public double getCooldownPercent(Player player) {
    double percent = cooldowns.getPercent(player.getUniqueId());
    return Math.min(100.0, percent * 100);  // 0..100%
}

// Получить оставшееся время в мс
public long getRemainingTime(Player player) {
    Expireable expireable = cooldowns.entries().get(player.getUniqueId());
    return expireable != null ? expireable.getExpireAfterTime() : 0;
}
```

---

### ToggleSet

HashSet с методом `toggle()` — добавляет элемент, если его нет, и удаляет, если он есть:

```java
ToggleSet<UUID> mutedPlayers = new ToggleSet<>();

// Включить/выключить мут
public void toggleMute(Player player) {
    boolean wasMuted = mutedPlayers.toggle(player.getUniqueId());
    
    if (wasMuted) {
        player.sendMessage(Component.text("Мут снят", NamedTextColor.GREEN));
    } else {
        player.sendMessage(Component.text("Вы получили мут", NamedTextColor.RED));
    }
}

// Проверить статус
public boolean isMuted(Player player) {
    return mutedPlayers.contains(player.getUniqueId());
}
```

---

### CountedSet

Структура для подсчёта дублей в коллекции:

```java
CountedSet<String> votes = new CountedSet<>();

votes.

add("Steve");
votes.

add("Alex");
votes.

add("Steve");  // Steve проголосовал дважды

int steveVotes = votes.quantity("Steve");  // 2
int totalVotes = votes.sum();  // 3

// Найти игрока с максимальным количеством голосов
Map.Entry<String, Integer> winner = votes.maxEntry();
// winner.getKey() = "Steve", winner.getValue() = 2

double average = votes.avg();  // 1.5
```

---

### IndexList

Список с индексами для быстрого поиска по разным ключам:

```java
// Создаём индексированный список пользователей
IndexList<User, UUID, String> users = new IndexList<>(
    MapIndex.byKey("byId", User::getId),      // индекс по UUID
    MapIndex.byKey("byName", User::getName)   // индекс по имени
  );

// Добавляем пользователей
users.

add(new User(uuid1, "Steve"));
  users.

add(new User(uuid2, "Alex"));

// Мгновенный поиск по любому индексу
User byId = users.getBy("byId", uuid1);
User byName = users.getBy("byName", "Alex");

// Индексы перестраиваются автоматически при изменении списка
```

---

### PagedCollectionWrapper

Разбиение коллекции на страницы:

```java
List<ItemStack> shopItems = getShopItems();  // 100 предметов
PagedCollectionWrapper<ItemStack> pager = new PagedCollectionWrapper<>(shopItems, 9);  // 9 предметов на страницу

int totalPages = pager.getPagesAmount();  // 12 страниц

// Показать страницу 1 (0-based)
List<ItemStack> page1 = pager.getPage(1);
```

---

## 🛠️ Предметы и инвентари

### ItemBuilder

Fluent API для создания и модификации ItemStack:

```java
ItemStack legendarySword = ItemBuilder.of(Material.NETHERITE_SWORD)
                                      .name(Component.text("⚔️ Клинок Бездны")
                                                     .color(NamedTextColor.DARK_PURPLE)
                                                     .decoration(TextDecoration.ITALIC, false))
                                      .description(
                                        Component.text("Древнее оружие, полное тёмной энергии").color(NamedTextColor.GRAY),
                                        Component.empty(),
                                        Component.text("Нажми ПКМ для активации").color(NamedTextColor.GOLD)
                                      )
                                      .amount(1)
                                      .enchantment(Enchantment.SHARPNESS, 10)
                                      .enchantment(Enchantment.FIRE_ASPECT, 2)
                                      .unbreakable()
                                      .flags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES)
                                      .customModelData(1337)
                                      .setAttribute(Attribute.GENERIC_ATTACK_DAMAGE, 15.0)
                                      .build();

// Редактирование существующего предмета
ItemStack edited = ItemBuilder.of(existingItem)
                              .name(Component.text("Новое название"))
                              .build();
```

**Работа с головами:**

```java
ItemStack head = ItemBuilder.of(Material.PLAYER_HEAD)
                            .playerProfile(player.getPlayerProfile())  // голова игрока
                            .name(Component.text("Голова " + player.getName()))
                            .build();
```

**Работа с зельями:**

```java
ItemStack potion = ItemBuilder.of(Material.POTION)
                              .customEffect(new PotionEffect(PotionEffectType.SPEED, 6000, 1))  // 5 минут
                              .customEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 6000, 2))
                              .build();
```

---

### ActiveItem

Предметы с действиями по клику — мини-библиотека для создания кастомных предметов:

```java
// Телепортационный посох
new ActiveItemBuilder()
    .

predicate(item ->item.

getType() ==Material.BLAZE_ROD 
                    &&item.

hasItemMeta()
                    &&item.

getItemMeta().

hasDisplayName())
  .

bind(ItemAction.RIGHT_CLICK, event ->{
Player player = event.getPlayer();
Location target = player.getLocation().add(player.getDirection().multiply(10));
        player.

teleport(target);
        player.

getWorld().

playEffect(target, Effect.ENDER_DRAGON_BREATH, 0);
        player.

sendMessage(Component.text("🔥 Телепортация!", NamedTextColor.GOLD));
  })
  .

register(plugin);

// Лечебный предмет
new

ActiveItemBuilder()
    .

predicate(item ->item.

getType() ==Material.GOLDEN_APPLE)
  .

bind(List.of(ItemAction.RIGHT_CLICK, ItemAction.LEFT_CLICK),event ->{
Player player = event.getPlayer();
        player.

setHealth(Math.min(player.getMaxHealth(),player.

getHealth() +4));
  player.

getWorld().

playSound(player.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1,1);
  event.

getItem().

setAmount(event.getItem().

getAmount() -1);  // потребить предмет
  })
  .

register(plugin);
```

**ItemAction:** `CLICK_RIGHT`, `CLICK_LEFT`, `DROP`, `SWAP_OFFHAND`, `CRAFT`, `PICKUP`

---

### InventorySerializer

Сериализация инвентарей в Base64 для сохранения:

```java
// Сохранить инвентарь
String saved = InventorySerializer.toBase64(player.getInventory());
// Сохранить в БД или файл

// Восстановить инвентарь
Inventory inventory = InventorySerializer.fromBase64(saved);
player.

getInventory().

setContents(inventory.getContents());
```

---

### ItemStackUtils

Полезные утилиты для работы с предметами:

```java
// Редактирование меты
ItemStackUtils.editMeta(item, meta ->{
  meta.

setCustomModelData(123);
});

// Удалить курсив из названия (по умолчанию в Minecraft)
ItemStack noItalic = ItemStackUtils.removeItalicFont(item);

// Сравнение предметов с гибкими настройками
boolean similar = ItemStackUtils.isSimilar(item1, item2,
                                           SimilarMode.IGNORE_AMOUNT,      // игнорировать количество
                                           SimilarMode.IGNORE_DURABILITY   // игнорировать прочность
);

// Сериализация в SNBT (строковый NBT)
String snbt = ItemStackUtils.toSNBT(item);
ItemStack restored = ItemStackUtils.fromSNBT(snbt);
```

---

## ⏱️ Время и даты

### Durations

Константы длительностей в миллисекундах:

```java
// Готовые константы
long tick = Durations.TICK;           // 50 мс
long second = Durations.SECOND;       // 1000 мс
long minute = Durations.MINUTE;       // 60000 мс
long hour = Durations.HOUR;           // 3600000 мс
long day = Durations.DAY;             // 86400000 мс
long minecraftDay = Durations.MINECRAFT_DAY;  // 1200000 мс (20 минут)

// Использование
Bukkit.

getScheduler().

runTaskLater(plugin, task, Durations.MINUTE /50);  // через 1 минуту
```

---

### Expireable

Таймер обратного отсчёта для cooldown'ов:

```java
Expireable cooldown = new Expireable(Durations.MINUTE * 5);  // 5 минут

// Сбросить таймер на текущее время
cooldown.

reset();

// Проверить, истёк ли
if(cooldown.

isExpired()){

useAbility();
    cooldown.

reset();  // перезапустить
}

// Получить процент выполнения (0..1+, где >=1 — истёк)
double percent = cooldown.getPercent();

// Получить обратный процент (1..0, где 0 — истёк)
double remaining = cooldown.getRevertPercentBounded();

// Получить оставшееся время в мс
long remainingMs = cooldown.getExpireAfterTime();
```

---

### StringToDuration

Парсинг строк вида `"1h 30m 15s"` в Duration:

```java
// Парсинг
Duration duration = StringToDuration.parse("1h 30m");  // 90 минут
Duration days = StringToDuration.parse("5d");          // 5 дней
Duration complex = StringToDuration.parse("1d 2h 3m 10s");

// Обратное преобразование
String formatted = StringToDuration.fromDuration(duration);  // "1ч 30м"

// Проверка валидности
boolean valid = StringToDuration.isValid("5h");    // true
boolean invalid = StringToDuration.isValid("abc"); // false
```

**Поддерживаемые единицы:** `s`, `sec`, `second`, `m`, `min`, `minute`, `h`, `hour`, `d`, `day`, `w`, `week`, `mo`,
`month`, `y`, `year`

---

### TimeFormatter

Форматирование времени в человекочитаемый вид:

```java
Duration duration = Duration.ofMillis(7320000);  // 2 часа 2 минуты

// Полное форматирование
String formatted = TimeFormatter.getFormattedTime(duration);  // "2 ч 2 мин"
String limited = TimeFormatter.getFormattedTime(duration, 1);  // "2 ч" (только 1 единица)

// Форматирование timestamp
String fullDate = TimeFormatter.formatFull(System.currentTimeMillis());    // "2024.03.14 15:30:45"
String timeOnly = TimeFormatter.formatHours(System.currentTimeMillis());   // "15:30:45"

// Текущее время
String now = TimeFormatter.formatNowFull();
```

---

## 🔤 Текст

### StringUtils

Утилиты для работы со строками:

```java
// Поиск похожих строк (для автокомплита команд)
List<String> variants = List.of("teleport", "tell", "time", "tp", "top");
List<String> suggestions = StringUtils.getSuggestions(variants, "te", 5);
// Результат: ["tell", "teleport", "time"]

// Проверка на безопасную строку (только буквы, цифры, подчёркивания)
boolean safe = StringUtils.isSafetyString("username123");   // true
boolean unsafe = StringUtils.isSafetyString("admin; rm");   // false

// Разбиение строки на части равной длины
List<String> parts = StringUtils.splitEqually("Длинный текст для разбивки", 5);
// ["Длинн", "ый те", "кст д", "ля ра", "збивк", "и"]

// Поиск с учётом регистра
boolean contains = StringUtils.containsIgnoreCase("Hello World", "world");  // true
boolean startsWith = StringUtils.startWithIgnoreCase("Hello", "HELLO");     // true
boolean endsWith = StringUtils.endsWithIgnoreCase("Hello.txt", ".TXT");     // true

// Сплит с сохранением пустых элементов
String[] args = StringUtils.safetySplit("arg1,arg2,", ",");  // ["arg1", "arg2", ""]
```

---

### Declinations

Склонение слов по числам (русский язык):

```java
// Регистрация слова
Declinations.registerForNumbers("coin", "монета", "монеты", "монет");
Declinations.registerForNumbers("player", "игрок", "игрока", "игроков");
Declinations.registerForNumbers("item", "предмет", "предмета", "предметов");

// Использование
int coins = 5;
player.sendMessage(Component.text("У вас " + coins + " " + Declinations.cast("coin", coins)));
// "У вас 5 монет"

int players = 21;
player.sendMessage(Component.text(players + " " + Declinations.cast("player", players)));
// "21 игрок"

// Примеры склонений:
// 1 монета, 2 монеты, 5 монет, 21 монета, 25 монет
// 1 игрок, 2 игрока, 5 игроков, 21 игрок, 25 игроков
```

---

### NanoID

Генерация компактных уникальных идентификаторов:

```java
// Стандартный NanoID (21 символ, URL-safe)
String id = NanoID.randomNanoId();  // "v7Hx-9kLm2pQ4sT8wN3jR"

// Кастомная длина
String shortId = NanoID.randomNanoId(8);   // "Kx7mP2nQ"
String longId = NanoID.randomNanoId(32);   // "abc123def456..."

// Кастомный алфавит
char[] alphabet = "ABCDEF0123456789".toCharArray();
String hexId = NanoID.randomNanoId(new SecureRandom(), alphabet, 16);
```

---

## 📊 Утилиты

### Benchmark

Замер производительности кода:

```java
// Простой замер
Benchmark.getChannel("serialization").start();

// ... тестируемый код ...
serializeLargeObject(obj);

Benchmark.newIteration("serialization");  // завершить итерацию и начать новую

// После 200 итераций автоматически выведет статистику в консоль
// Или вручную остановить раньше:
Benchmark.print("serialization");

// Замер с стадиями
Benchmark.stage("myTest", "start");
// ... код ...
Benchmark.stage("myTest", "processing");
// ... код ...
Benchmark.stage("myTest", "finish");
Benchmark.newIteration("myTest");

// Настройки
Benchmark.stopAt(500);  // установить лимит итераций (по умолчанию 200)
Benchmark.disable();    // временно отключить
Benchmark.enable();     // включить обратно
```

---

### Flow

Последовательное выполнение действий с условиями, циклами и задержками:

```java
// Простая цепочка
Flow<Player> flow = Flow.of(player)
    .next(p -> p.sendMessage("Начинаем телепортацию..."))
    .sleep(1000)  // ждём 1 секунду
    .next(p -> {
        Location loc = p.getLocation().add(0, 10, 0);
        p.teleport(loc);
    })
    .next(p -> p.sendMessage("Телепортация завершена!"));

flow.startSync(plugin);

// С условиями
Flow.of(player)
    .doIf(p -> p.teleport(spawn), p -> p.isOnline())  // телепортировать только если онлайн
    .doIf(p -> p.getInventory().addItem(item), p -> p.hasPermission("give.item"))
    .startHere(plugin);

// С циклом
Flow.of(counter)
    .doUntil(c -> {
        c.increment();
        plugin.getLogger().info("Счётчик: " + c.get());
    }, c -> c.get() < 10)  // выполнять пока счётчик < 10
    .startSync(plugin);

// Асинхронный запуск с задержкой
flow.startAsync(plugin, 20);  // через 20 тиков (1 секунда)

// Периодический запуск
flow.startSyncTimer(plugin, 0, 100);  // каждые 100 тиков (5 секунд)

// Отключить подавление исключений
Flow.of(player)
    .doNotSuppressExceptions()
    .next(p -> riskyOperation(p))
    .startSync(plugin);
```

---

### Scheduler

Fluent API для планировщика задач:

```java
// Простой планировщик
Scheduler.create()
    .

perform(() ->plugin.

getLogger().

info("Тик!"))
  .

register(plugin, 0);  // задержка 0 тиков

// С условием выполнения
Scheduler.

create(player)
    .

perform(p ->p.

setHealth(p.getHealth() +1))
  .

until(p ->p.

getHealth() >=p.

getMaxHealth())  // выполнять пока здоровье не полное
  .

register(plugin, 20);  // каждые 20 тиков (1 секунда)

// Асинхронный планировщик
Scheduler.

create()
    .

async()
    .

perform(() ->

heavyComputation())
  .

register(plugin, 0);

// Бесконечный (не отменять после выполнения условия)
Scheduler.

create(counter)
    .

perform(c ->c.

increment())
  .

until(c ->c.

get() < 100)
  .

infinite()  // не отменять, даже если условие false
    .

register(plugin, 1);

// Действие при невыполнении условия
Scheduler.

create(player)
    .

perform(p ->

useAbility(p))
  .

until(p ->cooldowns.

isReady(p))
  .

otherwise(p ->p.

sendMessage("Способность ещё не готова!"))
  .

register(plugin, 10);

// VoidScheduler (без объекта)
VoidScheduler.

create()
    .

perform(() ->Bukkit.

getOnlinePlayers().

forEach(this::checkPlayer))
  .

register(plugin, 20);
```

---

### Cache

Кэш с авто-обновлением по времени:

```java
// Создать кэш с обновлением раз в 5 секунд
Cache<Integer> onlineCache = Cache.of(() -> Bukkit.getOnlinePlayers().size(), Duration.ofSeconds(5));

// Получить значение (авто-обновление если прошло 5 секунд)
int online = onlineCache.get();

// Принудительное обновление
onlineCache.

update();

// Пример с базой данных
Cache<UserProfile> profileCache = Cache.of(() -> database.loadProfile(userId), Duration.ofMinutes(10));

UserProfile profile = profileCache.get();  // загрузится из БД не чаще чем раз в 10 минут
```

---

### ConfirmAPI

Система подтверждений действий через чат:

```java
// Запросить подтверждение
ConfirmAPI.requestBuilder(player, "/deletebase confirm", 600)  // 600 тиков = 30 секунд
    .success(() -> {
        deleteBase(player);
        player.sendMessage(Component.text("База удалена!", NamedTextColor.GREEN));
    })
    .expired(() -> {
        player.sendMessage(Component.text("Время подтверждения истекло", NamedTextColor.RED));
    })
    .register(plugin);

player.sendMessage(Component.text("Введите /deletebase confirm для подтверждения"));

// Проверка статуса
if (ConfirmAPI.senderAffected(player)) {
    String required = ConfirmAPI.getString(player);
    player.sendMessage(Component.text("Ожидается подтверждение: " + required));
}

// Обработка ввода игрока (автоматически через ChatOutListener)
@EventHandler
public void onChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    String message = event.getMessage();
    
    if (ConfirmAPI.senderAffected(player) && message.equals(ConfirmAPI.getString(player))) {
        ConfirmAPI.onSuccess(player);
        event.setCancelled(true);
    }
}
```

---

### EventHandler

Временные слушатели событий с авто-отпиской:

```java
// Одноразовый слушатель (отписывается после первого события)
new EventHandler<>(PlayerMoveEvent.class, plugin, event -> {
    Player player = event.getPlayer();
    player.sendMessage("Вы сделали шаг!");
    return true;  // true = отписаться после этого события
});

// Слушатель с ручной отпиской
EventHandler<PlayerInteractEvent> handler = new EventHandler<>(PlayerInteractEvent.class, event -> {
    Player player = event.getPlayer();
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        player.sendMessage("ПКМ по блоку!");
        handler.unregister();  // отписаться
    }
}, plugin);

// Слушатель с приоритетом
new EventHandler<>(EntityDamageEvent.class, EventPriority.HIGHEST, plugin, event -> {
    if (event.getEntity() instanceof Player) {
        // обработка урона
    }
});

// Обычный слушатель (без авто-отписки)
new EventHandler<>(PlayerQuitEvent.class, event -> {
    cleanup(event.getPlayer());
}, plugin);
```

---

### ParticlesUtils

Отрисовка частиц:

```java
// Линия частиц
ParticlesUtils.drawLineParticles(startLoc, endLoc,
                                 ParticlesUtils.getRedstoneParticle(Color.RED, 2),  // красные частицы
    0.5  // расстояние между частицами
      );

// Полый кубоид из частиц
      ParticlesUtils.

drawHollowCuboid(corner1, corner2, Particle.FLAME, 0.3);

// Кастомные Redstone-частицы
Particle.DustOptions dust = ParticlesUtils.getRedstoneParticle(Color.fromRGB(255, 100, 50), 3);
player.

getWorld().

spawnParticle(dust, location, 10);
```

---

### PlayerUtils

Утилиты для работы с игроками:

```java
// Выдать предмет (если нет места — дроп под ноги)
PlayerUtils.safeGive(player, item);

// Удалить предметы из инвентаря
boolean removed = PlayerUtils.removeItems(player, Material.DIAMOND, 10);

// Посчитать количество предметов
int amount = PlayerUtils.getItemAmount(player, Material.GOLD_INGOT);

// Получить ближайших сущностей
List<LivingEntity> nearby = PlayerUtils.getNearbyLivingEntities(player, 5.0);
Optional<LivingEntity> nearest = PlayerUtils.getNearbyLivingEntity(player, 3.0);

// Нанести урон с учётом брони
PlayerUtils.

damagePlayer(player, 5.0);

// Получить опыт игрока
int exp = PlayerUtils.getPlayerExp(player);
PlayerUtils.

changePlayerExp(player, -100);  // отнять 100 опыта
```

---

### NumbersUtils / Rand

Утилиты для чисел и рандома:

```java
// NumbersUtils
double rounded = NumbersUtils.round(3.14159, 2);  // 3.14
int percent = NumbersUtils.toPercent(0.75);       // 75
boolean isNum = NumbersUtils.isNumber("123");     // true
int bounded = NumbersUtils.bound(150, 0, 100);    // 100
String formatted = NumbersUtils.format(1234567);  // "1 234 567"
String shortFmt = NumbersUtils.shortNumberFormat(1500);  // "1.5k"

// Rand
int randomInt = Rand.ofInt(100);           // 0..99
int randomRange = Rand.ofInt(10, 20);      // 10..19
long randomLong = Rand.ofLong(1L, 100L);   // 1..99
double randomDouble = Rand.ofDouble(0, 1); // 0.0..1.0
boolean randomBool = Rand.bool();          // true/false
boolean chance = Rand.testChance(0.25);    // 25% шанс
```

---

### LocationUtils

Утилиты для работы с локациями:

```java
// Случайная локация в области
Location random = LocationUtils.randomLocation(pos1, pos2);

// Получить все блоки между точками
List<Block> blocks = LocationUtils.getAllBlocksBetween(loc1, loc2);

// Проверить, есть ли блок между точками (raytrace)
boolean hasBlock = LocationUtils.hasBlockBlockBetween(loc1, loc2);

// Сущности в радиусе
Collection<LivingEntity> entities = LocationUtils.getEntitiesInRadius(location, 5.0, false);

// Проверка, находится ли точка в кубоиде
boolean inRegion = LocationUtils.isInRegion(tested, regionPos1, regionPos2);

// Получить BlockFace из yaw
BlockFace face = LocationUtils.getBlockFaceFromYaw(player.getLocation().getYaw());

// Упаковать координаты чанка в int
int packed = LocationUtils.packChunkCoords(location);
int x = LocationUtils.unpackChunkX(packed);
int z = LocationUtils.unpackChunkZ(packed);
```

---

## 📚 Полный список утилит

### Анимации

- [`SimpleAnimation`](src/main/java/ru/cwcode/cwutils/animation/SimpleAnimation.java) — простая анимация значений
- [`ObjectAnimation`](src/main/java/ru/cwcode/cwutils/animation/ObjectAnimation.java) — анимация объектов
- [`AnimationProperties`](src/main/java/ru/cwcode/cwutils/animation/AnimationProperties.java) — настройки анимации
- [`TimingFunction`](src/main/java/ru/cwcode/cwutils/animation/timingFunction/TimingFunction.java) — функции плавности
- [`CubicBezier`](src/main/java/ru/cwcode/cwutils/animation/timingFunction/cubicBezier/CubicBezier.java) — кривые Безье

### Коллекции

- [`ExpiredSet`](src/main/java/ru/cwcode/cwutils/collections/ExpiredSet.java) — коллекция с истечением элементов
- [`ToggleSet`](src/main/java/ru/cwcode/cwutils/collections/ToggleSet.java) — set с методом toggle()
- [`IndexList`](src/main/java/ru/cwcode/cwutils/collections/indexList/IndexList.java) — индексированный список
- [`CountedSet`](src/main/java/ru/cwcode/cwutils/collections/CountedSet.java) — set с подсчётом вхождений
- [`PagedCollectionWrapper`](src/main/java/ru/cwcode/cwutils/collections/PagedCollectionWrapper.java) — пагинация
  коллекции
- [`CollectionUtils`](src/main/java/ru/cwcode/cwutils/collections/CollectionUtils.java) — утилиты коллекций
- [`EnumUtils`](src/main/java/ru/cwcode/cwutils/collections/EnumUtils.java) — утилиты enum'ов
- [`MinecraftEnums`](src/main/java/ru/cwcode/cwutils/collections/MinecraftEnums.java) — перечисления Minecraft

### Предметы

- [`ItemBuilder`](src/main/java/ru/cwcode/cwutils/items/ItemBuilder.java) — конструктор предметов
- [`ActiveItem`](src/main/java/ru/cwcode/cwutils/items/activeItem/ActiveItem.java) — активные предметы с действиями
- [`ItemStackUtils`](src/main/java/ru/cwcode/cwutils/items/ItemStackUtils.java) — утилиты ItemStack
- [`InventorySerializer`](src/main/java/ru/cwcode/cwutils/items/InventorySerializer.java) — сериализация инвентарей
- [`ItemBuilderFactory`](src/main/java/ru/cwcode/cwutils/items/ItemBuilderFactory.java) — фабрика билдеров
- [`ItemTagHelper`](src/main/java/ru/cwcode/cwutils/items/ItemTagHelper.java) — работа с NBT-тегами
- [`ItemTypes`](src/main/java/ru/cwcode/cwutils/items/ItemTypes.java) — типы предметов
- [`Craftable`](src/main/java/ru/cwcode/cwutils/items/Craftable.java) — крафтуемые предметы
- [`CompareService`](src/main/java/ru/cwcode/cwutils/items/compare/CompareService.java) — сравнение предметов
- [`RechargeableItem`](src/main/java/ru/cwcode/cwutils/items/rechargable/RechargeableItem.java) — предметы с
  перезарядкой

### Сущности и мир

- [`Hologram`](src/main/java/ru/cwcode/cwutils/entity/Hologram.java) — текстовые голограммы
- [`EntityUtils`](src/main/java/ru/cwcode/cwutils/entity/EntityUtils.java) — утилиты сущностей
- [`FireworkUtils`](src/main/java/ru/cwcode/cwutils/entity/FireworkUtils.java) — запуск фейерверков
- [`DamageCalculator`](src/main/java/ru/cwcode/cwutils/entity/DamageCalculator.java) — расчёт урона
- [`ParticlesUtils`](src/main/java/ru/cwcode/cwutils/particles/ParticlesUtils.java) — спавн частиц
- [`WorldUtils`](src/main/java/ru/cwcode/cwutils/world/WorldUtils.java) — создание миров
- [`WorldEditUtils`](src/main/java/ru/cwcode/cwutils/worldEdit/WorldEditUtils.java) — интеграция WorldEdit
- [`WorldGuardUtils`](src/main/java/ru/cwcode/cwutils/worldguard/WorldGuardUtils.java) — интеграция WorldGuard

### Игроки

- [`PlayerUtils`](src/main/java/ru/cwcode/cwutils/player/PlayerUtils.java) — утилиты игроков
- [`PlayerFaceResolver`](src/main/java/ru/cwcode/cwutils/player/PlayerFaceResolver.java) — получение скина игрока
- [`PlayerHider`](src/main/java/ru/cwcode/cwutils/player/hidder/PlayerHider.java) — скрытие игроков
- [`HideOption`](src/main/java/ru/cwcode/cwutils/player/hidder/HideOption.java) — опции скрытия
- [`WindowIdCatcher`](src/main/java/ru/cwcode/cwutils/player/WindowIdCatcher.java) — перехват ID окна

### Сообщения

- [`Message`](src/main/java/ru/cwcode/cwutils/messages/Message.java) — обёртка сообщений
- [`MessagesUtils`](src/main/java/ru/cwcode/cwutils/messages/MessagesUtils.java) — утилиты сообщений
- [`Placeholder`](src/main/java/ru/cwcode/cwutils/messages/Placeholder.java) — плейсхолдеры
- [`MessageReturn`](src/main/java/ru/cwcode/cwutils/messages/MessageReturn.java) — возврат сообщений

### Конфигурация

- [`SimpleConfig`](src/main/java/ru/cwcode/cwutils/config/SimpleConfig.java) — простой конфиг
- [`ConfigUtils`](src/main/java/ru/cwcode/cwutils/config/ConfigUtils.java) — утилиты конфигов ⚠️ deprecated
- [`ItemStackConstituents`](src/main/java/ru/cwcode/cwutils/config/ItemStackConstituents.java) — сериализация предметов

### Время и даты

- [`Durations`](src/main/java/ru/cwcode/cwutils/datetime/Durations.java) — константы времени
- [`TimeFormatter`](src/main/java/ru/cwcode/cwutils/datetime/TimeFormatter.java) — форматирование времени
- [`StringToDuration`](src/main/java/ru/cwcode/cwutils/datetime/StringToDuration.java) — парсинг длительностей
- [`Expireable`](src/main/java/ru/cwcode/cwutils/datetime/Expireable.java) — таймер обратного отсчёта

### Текст

- [`StringUtils`](src/main/java/ru/cwcode/cwutils/text/StringUtils.java) — утилиты строк
- [`NanoID`](src/main/java/ru/cwcode/cwutils/text/nanoid/NanoID.java) — генерация уникальных ID
- [`Declinations`](src/main/java/ru/cwcode/cwutils/text/Declinations.java) — склонение слов
- [`ComponentGradient`](src/main/java/ru/cwcode/cwutils/text/component/ComponentGradient.java) — градиенты ⚠️ deprecated
- [`StringToObjectParser`](src/main/java/ru/cwcode/cwutils/text/StringToObjectParser.java) — парсинг строк в объекты
- [`StringLocationConverter`](src/main/java/ru/cwcode/cwutils/text/converter/StringLocationConverter.java) — конвертация
  локаций

### Планировщик

- [`Scheduler`](src/main/java/ru/cwcode/cwutils/scheduler/Scheduler.java) — fluent API планировщика
- [`VoidScheduler`](src/main/java/ru/cwcode/cwutils/scheduler/VoidScheduler.java) — планировщик без объекта
- [`Tasks`](src/main/java/ru/cwcode/cwutils/scheduler/Tasks.java) — управление задачами
- [`RepeatAPI`](src/main/java/ru/cwcode/cwutils/scheduler/annotationRepeatable/RepeatAPI.java) — аннотации для повторов

### Утилиты

- [`Benchmark`](src/main/java/ru/cwcode/cwutils/benchmark/Benchmark.java) — бенчмаркинг
- [`Flow`](src/main/java/ru/cwcode/cwutils/flow/Flow.java) — поток действий
- [`Cache`](src/main/java/ru/cwcode/cwutils/cache/Cache.java) — кэширование
- [`ConfirmAPI`](src/main/java/ru/cwcode/cwutils/confirmable/ConfirmAPI.java) — подтверждения действий
- [`EventHandler`](src/main/java/ru/cwcode/cwutils/event/EventHandler.java) — временные слушатели
- [`EventUtils`](src/main/java/ru/cwcode/cwutils/event/EventUtils.java) — утилиты событий
- [`LocationUtils`](src/main/java/ru/cwcode/cwutils/location/LocationUtils.java) — утилиты локаций
- [`VectorUtils`](src/main/java/ru/cwcode/cwutils/vector/VectorUtils.java) — утилиты векторов
- [`NumbersUtils`](src/main/java/ru/cwcode/cwutils/numbers/NumbersUtils.java) — утилиты чисел
- [`Rand`](src/main/java/ru/cwcode/cwutils/numbers/Rand.java) — генератор случайных чисел
- [`ColorUtils`](src/main/java/ru/cwcode/cwutils/colors/ColorUtils.java) — утилиты цветов ⚠️ deprecated
- [`RGB`](src/main/java/ru/cwcode/cwutils/colors/RGB.java) — обёртка RGB
- [`BlockUtils`](src/main/java/ru/cwcode/cwutils/block/BlockUtils.java) — утилиты блоков
- [`CommandsUtils`](src/main/java/ru/cwcode/cwutils/commands/CommandsUtils.java) — утилиты команд
- [`Comparators`](src/main/java/ru/cwcode/cwutils/comparators/Comparators.java) — компараторы
- [`Conditions`](src/main/java/ru/cwcode/cwutils/conditions/Conditions.java) — условия
- [`FileUtils`](src/main/java/ru/cwcode/cwutils/files/FileUtils.java) — утилиты файлов
- [`PastesDevClient`](src/main/java/ru/cwcode/cwutils/files/PastesDevClient.java) — клиент pastes.dev
- [`Logger`](src/main/java/ru/cwcode/cwutils/logger/Logger.java) — логгер
- [`ReflectionUtils`](src/main/java/ru/cwcode/cwutils/reflection/ReflectionUtils.java) — рефлексия
- [`Injector`](src/main/java/ru/cwcode/cwutils/reflection/injector/Injector.java) — внедрение зависимостей
- [`ClassScanner`](src/main/java/ru/cwcode/cwutils/reflection/ClassScanner.java) — сканирование классов
- [`ServerUtils`](src/main/java/ru/cwcode/cwutils/server/ServerUtils.java) — утилиты сервера
- [`EnvUtils`](src/main/java/ru/cwcode/cwutils/system/EnvUtils.java) — утилиты окружения
- [`MatrixUtils`](src/main/java/ru/cwcode/cwutils/matrix/MatrixUtils.java) — утилиты матриц
- [`XpUtils`](src/main/java/ru/cwcode/cwutils/xp/XpUtils.java) — утилиты опыта
- [`State`](src/main/java/ru/cwcode/cwutils/statefull/State.java) / [
  `StateHolder`](src/main/java/ru/cwcode/cwutils/statefull/StateHolder.java) — state machine
- [`DependencyChecker`](src/main/java/ru/cwcode/cwutils/dependencyChecker/DependencyChecker.java) — проверка
  зависимостей
- [`L10n`](src/main/java/ru/cwcode/cwutils/l10n/L10n.java) — локализация
- [`BungeeCordMainChannel`](src/main/java/ru/cwcode/cwutils/bungeecord/BungeeCordMainChannel.java) — BungeeCord каналы
- [`LegacyBossBarAdapter`](src/main/java/ru/cwcode/cwutils/bossBar/LegacyBossBarAdapter.java) — адаптер босс-баров
- [`Bounceable`](src/main/java/ru/cwcode/cwutils/bounceable/Bounceable.java) — прыгающие сущности
- [`Cache`](src/main/java/ru/cwcode/cwutils/cache/Cache.java) — кэширование
- [`ReloadCatcher`](src/main/java/ru/cwcode/cwutils/ReloadCatcher.java) — перехват перезагрузки

---

## 📦 Установка

### Maven

```xml
<repository>
    <id>codemc-repo</id>
    <url>https://repo.codemc.org/repository/maven-public/</url>
</repository>

<dependency>
    <groupId>ru.cwcode.cwutils</groupId>
    <artifactId>TkachUtils</artifactId>
    <version>VERSION</version>
    <scope>compile</scope>
</dependency>
```

### Gradle

```groovy
repositories {
    maven { url 'https://repo.codemc.org/repository/maven-public/' }
}

dependencies {
    implementation 'ru.cwcode.cwutils:TkachUtils:VERSION'
}
```

---

## ⚠️ Ограничения

- **ColorUtils** / **ComponentGradient** — не работают с новыми версиями Adventure (удалён метод `TextColor.lerp`)
- **ConfigUtils** — помечен как `@Deprecated(forRemoval = true)`, используйте `SimpleConfig`
- **PersonalBossBar** / **BroadcastBossBar** — требуют регистрации в менеджере для автоматического обновления

---

## 📝 Лицензия

[MIT License](LICENSE)
