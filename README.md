Movies Challenge
===

It's a sample app for the movies challenge.

Modules
---
This project implements the Clean Architecture. As you may know Clean Architecture has an onion shape structure like this

![clean-architecture](https://blog.cleancoder.com/uncle-bob/images/2012-08-13-the-clean-architecture/CleanArchitecture.jpg)

In this project we manage it like this

- `domain` module: the `Entity` and `Use Cases`.
- `data` module: the `Gateways`, `DB`, `External Interfaces` and `Device`.
- `presentation` module: the `MVVM` architectural pattern, which includes `UI`.
- `app` module: integrate all classes and assemble the apk file. Mainly the modules and providers of Dagger framework live here.