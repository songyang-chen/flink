import scala.util.Random

/**
  * author: jiaozhu
  * create: 2020-04-03-10:05
  * tel: 17717876906
  */
object Testd {
  def main(args: Array[String]): Unit = {
    //    val random = new Random()
    //    println(1.to(9).map(x => (x, x + 1, x + random.nextGaussian())))

    val list = List(1, 2, 3, 49)
    println(list.flatMap(x => List(x, x + 1)))


    val list2 = List("a b", "c d")
    val stringList: List[String] = list2.flatMap(x => x.split(" "))

    println(stringList)


  }

}
