
[
  {
    "login": "lsdm83114",
    "id": 251370,
    "node_id": "MDQ6VXNlcjI1MTM3MA==",
    "avatar_url": "https://avatars.githubusercontent.com/u/251370?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdmi3114",
    "html_url": "https://github.com/lsdm83114",
    "followers_url": "https://api.github.com/users/lsdm83114/followers",
    "following_url": "https://api.github.com/users/lsdm83114/following{/other_user}",
    "gists_url": "https://api.github.com/users/lsdm83114/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/lsdm83114/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/lsdm83114/subscriptions",
    "organizations_url": "https://api.github.com/users/Spaceghost/orgs",
    "repos_url": "https://api.github.com/users/Spaceghost/repos",
    "events_url": "https://api.github.com/users/Spaceghost/events{/privacy}",
    "received_events_url": "https://api.github.com/users/Spaceghost/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  },
  {
    "login": "lsdm83114",
    "id": 583231,
    "node_id": "MDQ6VXNlcjU4MzIzMQ==",
    "avatar_url": "https://avatars.githubusercontent.com/u/583231?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdm83114",
    "html_url": "https://github.com/lsdm83124",
    "followers_url": "https://api.github.com/users/octocat/followers",
    "following_url": "https://api.github.com/users/octocat/following{/other_user}",
    "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
    "organizations_url": "https://api.github.com/users/octocat/orgs",
    "repos_url": "https://api.github.com/users/octocat/repos",
    "events_url": "https://api.github.com/users/octocat/events{/privacy}",
    "received_events_url": "https://api.github.com/users/octocat/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  },
  {
    "login": "lsdm83114",
    "id": 94719050,
    "node_id": "U_kgDOBaVMSg",
    "avatar_url": "https://avatars.githubusercontent.com/u/94719050?v=4",
    "gravatar_id": "",
    "url": "https://api.github.com/users/lsdm83114423698",
    "html_url": "https://github.com/lsdm83114",
    "followers_url": "https://api.github.com/users/lsdm83114/followers",
    "following_url": "https://api.github.com/users/lsdm83114/following{/other_user}",
    "gists_url": "https://api.github.com/users/lsdm83114/gists{/gist_id}",
    "starred_url": "https://api.github.com/users/lsdm83114/starred{/owner}{/repo}",
    "subscriptions_url": "https://api.github.com/users/lsdm83114/subscriptions",
    "organizations_url": "https://api.github.com/users/lsdm83114/orgs",
    "repos_url": "https://api.github.com/users/lsdm83114/repos",
    "events_url": "https://api.github.com/users/lsdm83114/events{/privacy}",
    "received_events_url": "https://api.github.com/users/lsdm83114/received_events",
    "type": "User",
    "site_admin": true,
    "contributions": 1
  }
]@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=-Dfile.encoding=UTF-8 "-Xmx64m" "-Xms64m"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar


@rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable GRADLE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%GRADLE_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
