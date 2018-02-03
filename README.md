# Ninjabooks

### Description
Many well-developed companies provides some books and magazines for theirs employees. It helps them developing their knowledge, learning new skills and being in-think with the newest technologies.

However, this solution have many advantages, it brings some problems. If many employees want the same book there is a problem who was first in queue and how much time do they have to read it etc.
There are some programs which are used in libraries to control their collections of book but they need some administrators staff  and they are time-absorbing for consumers as well as administrators.
Our application is administrator-free and as simple in use as possible for consumers. Every book have unique QR code which is used to borrow the book and return it. If book you are interested in is
borrowed by someone else application will add you to waiting list and notify you when book will be available. After you end reading you can give your opinion about the book in comments sections.

### Technology stack
Backend:
* __Spring MVC + Spring Security + Spring Test__
* __Hibernate__ for ORM
* __MySQL__ as production database
* __HSQLDB__ as development database
* __Tomcat__ as webserver
* __Junit & Mockito__ for testing

Frontend:
* __Angular 4.0 witb Angular CLI__
* __Bootstrap__
* __nginx__
* __npm package manager__
* __karma__

Misc:
* __Docker__

### How to run
Maven is required if you want run only backend. In your terminal type:
```bash
    cd nb-backend
    mvn clean tomcat8:run-war
```

Then open your web browser and type
```bash
    localhost:8080
```

For runing frontend type in your terminal
```bash
    cd nb-frontend
    npm run start:proxy  
```

Then open your web browser and type

```bash
    localhost:3000
```

Rest tbd
