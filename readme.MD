# mame2es
Quick, dirty, and offline [Emulation Station](https://github.com/jrassa/EmulationStation) files generator from [MAME](https://github.com/mamedev/mame) data files.

Generates `resources/mamenames.xml` from MAME's `gamelist.txt`.

Algo generates `gamelists/<system>/gamelist.xml` with minimal metadata using from MAME's `--listxml`, and pointing to local snapshots and marquees.

## Usage

```
usage: java -jar mame2es.jar
  -help                 Shows usage
  -verbose              Verbose execution
  -dir <arg>            Input directory (optional; to generate gamelist.xml and custom-collections.cfg)
  -out <arg>            Output directory (optional; current directory by default)
  -gamelist <arg>       MAME gamelist.txt input file (optional)
  -listxml <arg>        MAME --listxml input file (optional)
  -catlist <arg>        progettoSNAPS.net MAME CATVER.ini input file (optional)
  -mamebioses <arg>     Emulation Station resources/mamebioses.xml input file (optional)
  -mamedevices <arg>    Emulation Station resources/mamedevices.xml input file (optional)
  -pathprefix <arg>     Emulation Station gamelist.xml <path> tag prefix (optional)
  -imageprefix <arg>    Emulation Station gamelist.xml <image> tag prefix (optional)
  -imagesuffix <arg>    Emulation Station gamelist.xml <image> tag suffix (optional)
  -marqueeprefix <arg>  Emulation Station gamelist.xml <marquee> tag prefix (optional)
  -marqueesuffix <arg>  Emulation Station gamelist.xml <marquee> tag suffix (optional)
```

## Compiling

`mvn verify`

Please note that the resource `data/mame64-0.220/listxml.xml` is not under source control because it is huge (241.205.820 bytes); re-create it using MAME CLI: `mame64.exe -listxml > listxml.xml`

---

Coded by [theNestruo](theNestruo@gmail.com)
