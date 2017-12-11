# Checkstyle extension for Visual Studio Code

## Prerequisites

* Please make sure ```Java``` is in system ```PATH```

## Commands
This extension provides several commands in the Command Palette (F1 or Ctrl + Shift + P) for working with Java files:
* **Checkstyle: Check code with Checkstyle**: Check current active Java file with Checkstyle.
* **Checkstyle: Set Checkstyle jar file path**: Set the jar file for Checkstyle.
* **Checkstyle: Set Checkstyle configuration file path**: Set the configuration file for Checkstyle.

## Options
```
{
    "checkstyle.autocheck": <boolean>
    "checkstyle.jarPath": "<string>",
    "checkstyle.configurationFile": "<string>"
    "checkstyle.propertiesPath": "<string>"
}
```

* ```checkstyle.autocheck``` - Specify whether the checkstyle extension will check Java files automatically.
* ```checkstyle.jarPath``` - Path to the checkstyle jar file. By default, the extension will use [checkstyle-8.0-all.jar](https://sourceforge.net/projects/checkstyle/files/checkstyle/8.0/)
* ```checkstyle.configurationFile``` - Specify the checkstyle configuration file. Entering ```google_checks``` will use [google_checks.xml](https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml). Entering ```sun_checks``` will use [sun_checks.xml](https://raw.githubusercontent.com/checkstyle/checkstyle/master/src/main/resources/google_checks.xml). Or you can specify the configuration file path in your local machine. By default, the extension will use ```google_checks```.
* ```checkstyle.propertiesPath``` - Path to the checkstyle properties file. By default is empty. If a property file is specified, the system properties are ignored. See the [-p option in checkstyle document](http://checkstyle.sourceforge.net/cmdline.html#Command_line_usage)

_If you want to use customized checkstyle config, please make sure the config rules are aligned with the checkstyle version._

## License
MIT