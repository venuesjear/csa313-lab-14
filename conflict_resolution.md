# Conflict шийдвэрлэлт

## Conflict-ийн дэлгэрэнгүй
`feature/remove-exception-dot` branch-ийг `develop` руу нэгтгэх үед `src/main/java/service/ReaderService.java` файлын `IllegalArgumentException` шиддэг мөрөн дээр merge conflict үүссэн.

Conflict нь тухайн exception-ийн текст хоёр branch-д өөрчлөгдсөнтэй холбоотой:

- `develop`: `"Reader already exists"`
- `feature/remove-exception-dot`: `"Reader with this ID already exists..."`

Git дараах байдлаар conflict-ийг харуулсан:
```
<<<<<<< HEAD
throw new IllegalArgumentException("Reader with this ID already exists...");
=======
throw new IllegalArgumentException("Reader already exists");
>>>>>>> develop
```

## Шийдвэрлэлтийн алхмууд

1. Feature-ийн branch-д `git merge develop` ажиллуулж conflict үүсгэсэн.
2. Conflict-ийг `ReaderService.java` файлыг нээж шалгасан.
3. Шийдвэрлэсэн хувилбарыг сонгож нэгтгэсэн:
```
throw new IllegalArgumentException("Reader already exists");
```
4. Засварласан файлыг stage-д оруулсан:
```commandline
git add src/main/java/service/ReaderService.java
```
5. Commit хийсэн:
```commandline
git commit -m "Resolve merge conflict in ReaderService exception message"
```
6. Branch push хийсэн:
```commandline
git push
```
7. PR үүсгэж, CI шалгалт даваагүй учраас PR-ийг хаасан.