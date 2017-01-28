# xray
xray is a team profiling tool based on team member's vcs behaviour. Its aim is to provide a technical approach on building better skilled teams. xray will identify certain _syndromes_ the team members may be affected by. You can find brief descriptions of them in an article I wrote a while back: https://www.todaysoftmag.com/article/2074/hyper-productive-teams

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

xray was build around the concept of a team.
