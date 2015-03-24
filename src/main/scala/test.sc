
import scala.sys.process._

val curlProcess = Process(Vector("curl", "-u https://api.zulip.com/v1/messages",
  "yelpbot-bot@students.hackerschool.com:NK3ilaGgyV9TxCikfEBbJVmBUKzNYKMx",
  "-d \"type=private\"",
"-d  \"to=ggilmore@mit.edu\"",
"-d \"content=I come not, friends, to steal away your hearts.\""))

curlProcess.run()

