# build-and-run.ps1

#Get rid of old binaries
Remove-Item -Recurse -Force bin

# Compile Java files
javac --module-path "C:\Users\karth\Documents\javafx-sdk-23\lib" `
      --add-modules javafx.controls,javafx.fxml `
      -cp "lib\ojdbc17.jar" `
      -d bin src/ORACLE_DBS_PROJECT/*.java

# Copy FXML files
Copy-Item src\ORACLE_DBS_PROJECT\*.fxml bin\ORACLE_DBS_PROJECT\

# Copy image folder (add this!)
Copy-Item -Recurse src\ORACLE_DBS_PROJECT\img bin\ORACLE_DBS_PROJECT\

# Run the program
java --module-path "C:\Users\karth\Documents\javafx-sdk-23\lib" `
     --add-modules javafx.controls,javafx.fxml `
     -cp "bin;lib\ojdbc17.jar" ORACLE_DBS_PROJECT.Main
