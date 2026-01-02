# Running the JavaFX desktop app

If you see `Error: JavaFX runtime components are missing`, Java was started without the JavaFX modules. Use one of the options below.

## Easiest: run via Maven (downloads JavaFX automatically)
```powershell
cd "C:\Users\TimothyNlenjibi\Downloads\Smart-E-Commerce-System-main (2)\Smart-E-Commerce-System-main"
mvn -DskipTests javafx:run
```
This uses the configured `javafx-maven-plugin` to set up the module path and launch `com.ecommerce.MainApp`.

## Run the packaged jar with a local JavaFX SDK
1) Download the matching JavaFX SDK (11.x) for Windows from https://openjfx.io/ and unzip, e.g. to `C:\javafx-sdk-11.0.2`.
2) Point `JAVA_FX_HOME` at the SDK `lib` folder and run with modules:
```powershell
cd "C:\Users\TimothyNlenjibi\Downloads\Smart-E-Commerce-System-main (2)\Smart-E-Commerce-System-main"
set JAVA_FX_HOME=C:\javafx-sdk-11.0.2\lib
mvn -DskipTests package
java --module-path "%JAVA_FX_HOME%" --add-modules javafx.controls,javafx.fxml,javafx.graphics -jar target\smart-ecommerce-system-1.0.0.jar
```

## Notes
- Use a JDK that does **not** bundle JavaFX (e.g., Temurin); the commands above supply the modules.
- Keep the JavaFX version aligned with the `javafx.version` property in `pom.xml`.
- If you add more JavaFX modules (media, web), list them in `--add-modules` or in the Maven plugin config.

