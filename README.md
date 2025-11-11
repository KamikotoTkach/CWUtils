# CWUtils 2.9.0
aka TkachUtils

Набор утилсов и мини-либ для всех случаев жизни.

Подключение:
```xml
<dependency>
  <groupId>ru.cwcode.cwutils</groupId>
  <artifactId>CWUtils</artifactId>
    <version>2.9.0</version>
</dependency>
```

Из интересного:
- Мини-либа на анимации
- Мини-либа на бенчмарки
- Хороший набор утилсов для коллекций
- Мульти-градиенты
- ConfirmableAPI - мини-либа на подтверждение действия пользователем
- Expireable/ExpireableSet - мега-полезные классы для всякого рода КД
- TimeFormatter - простой форматтер millis в нормальные форматы
- Мини-либа Flow - Последовательные действия над чем-то с использованием шедулеров, условий, циклов
- ItemBuilder - простое создание и модификация предмета через билдер
- Мини-либа на бинд действий к предмету (items/activeItem)
- NumbersUtils и Rand - утилсы для работы с числами, рандом
- PersistentHelper для работы с Persistent датой
- PlayerUtils с safeGive, подсчётом определённых предметов и удалением опр. кол-ва предметов с инвентаря, получения ближайших энтити без самого игрока
- ReflectionUtils для работы с рефлексией
- ClassScanner для сканирования всех классво плагина, его методов и полей
- Шедулер на основе BukkitTask-ов. (Scheduler.create(player).until(predicate).run(task).othewise(endTask))
- Аннотация Repeat(delay=ticks, async=true) для авто-рега тикающих шедулеров
- Ну и по мелочи всякого добра
<br><br>
Можно смело копировать в свои проекты, если не хочется добавлять зависимость
