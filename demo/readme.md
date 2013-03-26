Installing this project
-----------------------
1. Unpack Spring tool suite (STS) distribution into selected directory
2. Run STS
3. In shown dialog select desired workspace folder and press OK
4. From application menu select
    - Window -> Preferences
5. In shown tree menu select Maven -> installations
    - here should be selected maven installation which comes with STS package
6. Click on "Open file" link in the bottom of the window which opens Global settings from installation directory
  - Close preferences window
7. To see content of settings.xml file we need to close green Dashboard card in STS
8. In the bottom of shown file editor, click on "Source" card
9. Find text (Ctrl+f) localRepository and uncomment it.
10. Set repository forlder to folder on your disk

 `<!-- localRepository`
 `  | The path to the local repository maven will use to store artifacts.`
 `  |`
 `  | Default: ~/.m2/repository -->`
 ` <localRepository>D:/JAVA_REPOSITORY</localRepository>`
11. Save file
12. From STS menu select File -> Import -> Maven -> Existing Maven Projects
13. Click Next and Browse for Downloaded project folder.
14. Click Finish and project HIT2013 should appear in Package Explorer
15. From application menu select Run -> Run Configurations
16. Right click on Maven Build and select New
17. Name your build Command eg. Hit2013 Runner (name field)
18. Click on button Browse workspace and select root folder of project HIT2013. Base directory should be filled with ${workspace_loc:/hit2013}
19. In field Goals write clean compile tomcat:run which cleans workspace before each run, compile our application and deploy it to embeded tomcat.
20. Press button Run
21. Running the application triggers Maven which downloads all neccesery dependences (e.g. Spring framework) to your local repository specified in step 10. This may take a while, but does happen only once. After that the dependencies are used from local repository and can be used also for other projects.
22. If everything works fine, application should be running on http://localhost:8080/hit2013

