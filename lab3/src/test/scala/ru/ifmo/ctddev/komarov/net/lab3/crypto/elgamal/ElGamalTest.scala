/*
package ru.ifmo.ctddev.komarov.net.lab3.crypto.elgamal

import org.scalacheck._
import org.scalacheck.Prop._
import ru.ifmo.ctddev.komarov.net.lab3.bytes.BigIntToBytes

import Arbitrary._
import org.scalacheck

object ElGamalTest extends Properties("ElGamal") {

  property("BigIntToBytes") = forAll { (n : Int, x : BigInt) =>
    (0 < n && n < 1000 && x.bitLength < n) ==> {
      BigIntToBytes(n)(x).length == n
    }
  }

//  val myGen : Gen[(Int, BigInt)] = for {
//    x <- Arbitrary.arbInt
//    len <- Gen.choose(0, x)
//  } yield (x, BigInt)

  property("sqrt") = forAll { (d : Int) =>
    math.sqrt(d * d) == d
  }
//  val gg : Gen[(Int, BigInt)] = for (
//    n <- arbitrary[Int] suchThat (n => n > 1) ;
//    x <- Arbitrary[BigInt](Gen.choose(BigInt(2), BigInt(2).pow(n)))
//  ) yield (n, x)
//
//
//  val propBigIntToBytes = forAll(gg) { p =>
//    val n = p._1
//    val x = p._2
//    (n > 0 && n < 1000 && x > 0 && x.bitLength <= n) ==>
//      (BigIntToBytes(n)(x).length == n)
//  }
//
}

*/
