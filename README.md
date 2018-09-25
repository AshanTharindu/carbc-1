# CAR.bc
Inspired by the Ethereum Project. Under development - In fact, just started.

## Prerequisites
* Java 8
* Maven

## Instructions

### Add changes to this code
1. Fork this repository
2. Clone from your repository  
    `git clone https://github.com/YourUserName/carbc.git`
3. Go into the cloned directory
    `cd carbc`
4. Add your repository as the `origin`  
    `git remote add origin https://github.com/YourUserName/carbc.git`
5. Add this repository as the `upstream`  
    `git remote add upstream https://github.com/SuKSW/carbc.git`
6. Make changes as you wish
7. After making the changes: add, commit and push them to your repository.
    `git add <file_one> <file_two>`
    `git commit -m "the changes you made"`
    `git push origin master`
    
### Get the changes others have made
1. Fetch all the new changes from https://github.com/SuKSW/carbc.git which will have changes merged from other members' repositories as well.
    `git fetch upstream`
2. Make sure you are in the master branch
    `git checkout master`
3. Rebase your master branch. This will apply all the changes you made, on top of the existing version of the upstream branch.
    `git rebase upstream/master`
4. Send everything to your GitHub repository
    `git push origin master`
5. Go to GitHub and create a pull request. That's all.

## Run what you have built

All the commands below assumes that you are within the directory `carbc`.

### Using the IDE 
Easiest wat to test the inner components. Just create a class like `TestPeer` in `src/main/java` add a main method, right click on the main method and click run. 

Note: Yet this method will not work when testing the cli

### Build and run the jar
The Main method set for the jar, is the one in `src/main/java/Carbc`. The class that will be invoking the cli.

1. Clean and build the jar  
`mvn clean install`  
2. Run the jar  
`java -jar target/carbc-0.0.1-jar-with-dependencies.jar --help`

If you didn't like this long troublesome command, try running using the linux script.

### Build and run using a .sh script

1. Give execution permissions to the script `carbc.sh` (check the content of this file just in case, before continuing)
    * Right click on the script, select properties
    * Open the permissions tab
    * Allow executing file as a program
2. Run the script
    `./carbc.sh --help`
    
### Run within Docker
This will only print the help message for now.

1. Clean and build the jar  
   `mvn clean install`
2. Create and run the image  
    `docker build -f Dockerfile -t carbcimage .`  
    `docker run -p 8080:8080 -t carbcimage`


# CAR.BC_Exteneded
