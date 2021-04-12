[![](https://github.com/wutsi/site-server/actions/workflows/master.yml/badge.svg)](https://github.com/wutsi/site-server/actions/workflows/master.yml)

[![JDK](https://img.shields.io/badge/jdk-11-brightgreen.svg)](https://jdk.java.net/11/)
[![](https://img.shields.io/badge/maven-3.6-brightgreen.svg)](https://maven.apache.org/download.cgi)
![](https://img.shields.io/badge/language-kotlin-blue.svg)

This API manages the different sites manages by the Wutsi Platform.&#10;The API offer the following functionalities:&#10;- Registering new sites&#10;- Updating site informations&#10;- Managing the configuration of each sites&#10;This API give to the Platform the capability of multi-tenancy.&#10;

# Installation Prerequisites
## Database Setup
- Install postgres
- Create account with username/password: `postgres`/`postgres`
- Create a database named `site`

## Configure Github
- Generate a Github token for accessing packages from GibHub
  - Goto [https://github.com/settings/tokens](https://github.com/settings/tokens)
  - Click on `Generate New Token`
  - Give a value to your token
  - Select the permissions `read:packages`
  - Generate the token
- Set your GitHub environment variables on your machine:
  - `GITHUB_TOKEN = your-token-value`
  - `GITHUB_USER = your-github-user-name`

## Maven Setup
- Download Instance [Maven 3.6+](https://maven.apache.org/download.cgi)
- Add into `~/m2/settings.xml`
```
    <settings>
        ...
        <servers>
            ...
            <server>
              <id>github</id>
              <username>${env.GITHUB_USER}</username>
              <password>${env.GITHUB_TOKEN}</password>
            </server>
        </servers>
    </settings>
```

## Usage
- Install
```
$ git clone git@github.com:wutsi/site-server.git
```

- Build
```
$ cd site-server
$ mvn clean install
```

- Launch the API
```
$ mvn spring-boot:run
```

That's it... the API is up and running! Start sending requests :-)

# Links
- [API](https://wutsi.github.io/site-server/api/)
- [Documentation](docs/)
