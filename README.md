# xray
  xray is a team profiling tool based on team member's vcs behaviour. Its aim is to provide a technical approach on building better skilled teams. xray will identify certain _syndromes_ the team members may be affected by. You can find brief descriptions of these syndromes in an article I wrote a while back: https://www.todaysoftmag.com/article/2074/hyper-productive-teams

# vcs supported
  xray currently works only with Git. There are no concrete plans to make it work with other vcs at this stage.

# git export format
  The log input file must be exported in the folowing format:

  `git log --all --numstat --date=short --pretty=format:'--%h--%ad--%aN' --no-renames`

# build
  You can build the application using the following command:

  `mvn clean package`

  This will create an executable fatjar in the current directory called `xray.jar`.

# usage
  You can run the application using the following command:

  `java -jar xray.jar --log=LOG_EXPORT_FILE_NAME --filter="COMMA_SEPARATED_LIST_OF_TEAM_MEMBER_NAMES"`

  xray was build around the concept of a team. This is why it is mandatory to specify a list of commit authors in the `filter` parameter. Of course, the concept of a _team_ can have different meanings depending on what you want to analyse.
This is an example of a run:

`java -jar xray.jar --log=geolog.log --filter="Jack Doe, John Doe, Madalin Doe"`

# interpreting the reports
The main idea is to find a way to group the types of technical activities within the team. I've chosen to do this (for now at least) based of file extensions. You can configure this using the `extensions.properties` file. The keys in this file are the areas and the values are comma separated lists of file extensions that are part of those areas. There is a sample file in the repository, but you can tweak it as you want. The sample file contains:

```
Java=.java
ui=css,html,js,vm
jsp=jsp
sql=sql
config=xml,properties,yml
scripting=sh
```
There are also 3 implicit areas which cannot be configured at this stage:
- Unit Tests: based on file naming convention - the file with either start or end with _Test_
- Integration Tests: based on file naming convention - the file will either start or end with _IT_
- merges

A typical output report will look as follows:

```

+----------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+
| Author         | commit age       | total commits    | merges           | unit tests       | integ tests      | scripting        | java             | ui               | jsp              | config           | sql              |
+----------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+
| John D.        | 1461             | 912              | 250              | 176              | 2                | 2                | 1170             | 63               | 27               | 149              | 517              |
| Jack B.        | 1278             | 644              | 138              | 179              | 6                | 1                | 978              | 116              | 188              | 96               | 324              |
+----------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+------------------+

```
Where:
* _commit age_ is the number of days between today and the first commit date
* _total commits_ the total number of commits of the author
* for the rest of the areas, each number represents the total number of files changed within all author's commits for that particular area



# future plans
My target is to implement most of the _syndromes_ described in the article mentioned above, along with recommendations on how to take actions based on the results.
