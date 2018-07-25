-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 25, 2018 at 10:36 PM
-- Server version: 5.7.22-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `CarBC`
--

-- --------------------------------------------------------

--
-- Table structure for table `SmartContract`
--

CREATE TABLE `SmartContract` (
  `signature` varchar(512) NOT NULL,
  `contractName` varchar(128) NOT NULL,
  `code` blob NOT NULL,
  `owner` varchar(512) NOT NULL,
  `message` varchar(1024) NOT NULL,
  `block_number` int(200) NOT NULL,
  `block_timestamp` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
) ;

--
-- Dumping data for table `SmartContract`
--

INSERT INTO `SmartContract` (`signature`, `contractName`, `code`, `owner`, `message`, `block_number`, `block_timestamp`, `block_hash`, `vehicleId`) VALUES
('qq', 'VehicleContract', 0xcafebabe00000034007a0a001900370a00180038090039003a08003b0a003c003d0a0018003e0a003f00400a001800410a003f00420a003f00430a004400450a001800460800470a0048004907004a0a000f004b0a0048004c08004d08004e0a004f00500a004f00510a004f00520a004f00530700540700550100063c696e69743e010003282956010004436f646501000f4c696e654e756d6265725461626c6501000f726567697374657256656869636c65010029285b5b4c6a6176612f6c616e672f537472696e673b4c6a6176612f6c616e672f537472696e673b295a01000d537461636b4d61705461626c6501000a457863657074696f6e7307005607005707005807005907005a07005b01000f6368616e67654f776e657273686970010012636865636b547275654f776e65727368697001000328295a0100156164644d61696e74656e616e63655265636f72647301000d696e7375726556656869636c65010019636865636b56616c69646974794f6656616c69646174696f6e010014686578537472696e67546f427974654172726179010016284c6a6176612f6c616e672f537472696e673b295b4207005c01000c6765745075626c69634b657901002d284c6a6176612f6c616e672f537472696e673b294c6a6176612f73656375726974792f5075626c69634b65793b010006766572696679010030284c6a6176612f73656375726974792f5075626c69634b65793b5b424c6a6176612f6c616e672f537472696e673b295a01000a536f7572636546696c6501001456656869636c65436f6e74726163742e6a6176610c001a001b0c002d001f07005d0c005e005f01001968656c6c6f2066726f6d20746865206f7468657220736964650700600c006100620c003100320700630c006400650c003300340c006600670c0068006907006a0c006b006c0c002e002f01000344534107006d0c006e006f0100256a6176612f73656375726974792f737065632f58353039456e636f6465644b6579537065630c001a00700c0071007201000b534841317769746844534101000353554e0700730c006e00740c007500760c007700780c0033007901000f56656869636c65436f6e74726163740100106a6176612f6c616e672f4f626a6563740100266a6176612f73656375726974792f4e6f53756368416c676f726974686d457863657074696f6e0100136a6176612f696f2f494f457863657074696f6e0100206a6176612f73656375726974792f5369676e6174757265457863657074696f6e0100256a6176612f73656375726974792f4e6f5375636850726f7669646572457863657074696f6e0100216a6176612f73656375726974792f496e76616c69644b6579457863657074696f6e01002a6a6176612f73656375726974792f737065632f496e76616c69644b657953706563457863657074696f6e0100025b420100106a6176612f6c616e672f53797374656d0100036f75740100154c6a6176612f696f2f5072696e7453747265616d3b0100136a6176612f696f2f5072696e7453747265616d0100077072696e746c6e010015284c6a6176612f6c616e672f537472696e673b29560100106a6176612f6c616e672f537472696e67010008676574427974657301000428295b420100066c656e677468010003282949010006636861724174010004284929430100136a6176612f6c616e672f436861726163746572010005646967697401000528434929490100186a6176612f73656375726974792f4b6579466163746f727901000b676574496e7374616e636501002e284c6a6176612f6c616e672f537472696e673b294c6a6176612f73656375726974792f4b6579466163746f72793b010005285b42295601000e67656e65726174655075626c6963010037284c6a6176612f73656375726974792f737065632f4b6579537065633b294c6a6176612f73656375726974792f5075626c69634b65793b0100176a6176612f73656375726974792f5369676e617475726501003f284c6a6176612f6c616e672f537472696e673b4c6a6176612f6c616e672f537472696e673b294c6a6176612f73656375726974792f5369676e61747572653b01000a696e697456657269667901001c284c6a6176612f73656375726974792f5075626c69634b65793b2956010006757064617465010007285b4249492956010005285b42295a00210018001900000000000a0001001a001b0001001c0000001d00010001000000052ab70001b100000001001d000000060001000000070001001e001f0002001c00000036000300030000000d2a2b2cb600029a000503ac04ac00000002001d0000000e00030000000a0009000b000b000d00200000000300010b00210000000e000600220023002400250026002700010028001f0002001c00000026000200030000000ab200031204b6000504ac00000001001d0000000a0002000000120008001600210000000e000600220023002400250026002700010029002a0001001c0000001a000100010000000204ac00000001001d0000000600010000001a0001002b002a0001001c0000001a000100010000000204ac00000001001d0000000600010000001e0001002c002a0001001c0000001a000100010000000204ac00000001001d000000060001000000220001002d001f0002001c000000720003000600000032033e1d2bbea2002b2a2b1d320332b600063a042b1d320432b600073a05190419052cb800089a000503ac840301a7ffd504ac00000002001d0000001e0007000000260008002700130028001d002a0028002b002a00260030002e00200000000a0003fc00020127fa000500210000000e00060022002700250023002400260009002e002f0001001c0000007c00060004000000392ab600093c1b056cbc084d033e1d1ba200282c1d056c2a1db6000a1010b8000b07782a1d0460b6000a1010b8000b609154840302a7ffd92cb000000002001d0000001e00070000003200050033000b00340012003500260036003100340037003800200000000d0002fe000d0107003001fa00290001003100320002001c00000040000300050000001c2bb8000c4d120db8000e4ebb000f592cb700103a042d1904b60011b000000001001d0000001200040000003c0005003d000b003e0015003f00210000000a000400270022002300250009003300340002001c00000045000400040000002112121213b800144e2d2ab600152d2cb60007032cb60007beb600162d2bb60017ac00000001001d0000001200040000004300080044000d0045001b004600210000000a0004002500220026002400010035000000020036, 'qq', 'qq', 5, '2018-07-25 16:55:55.189000', 'aaa', 'qq');

-- --------------------------------------------------------

--
-- Table structure for table `VehicleHistory`
--

CREATE TABLE `VehicleHistory` (
  `id` int(11) NOT NULL,
  `vid` varchar(50) NOT NULL,
  `transaction_id` varchar(128) NOT NULL,
  `block_id` int(128) NOT NULL,
  `block_hash` varchar(256) NOT NULL,
  `event` varchar(50) NOT NULL,
  `sender` varchar(512) NOT NULL,
  `validation_array` varchar(1028) NOT NULL,
  `data` varchar(1028) NOT NULL,
  `smartContractSignature` varchar(526) NOT NULL,
  `smartContractMethod` varchar(128) NOT NULL,
  `parameters` varchar(526) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `VehicleHistory`
--

INSERT INTO `VehicleHistory` (`id`, `vid`, `transaction_id`, `block_id`, `block_hash`, `event`, `sender`, `validation_array`, `data`, `smartContractSignature`, `smartContractMethod`, `parameters`) VALUES
(1, 'a', 'q', 1, 'q', 'q', 'q', 'q', 'q', 's', 's', 's'),
(2, 'sa', 'q', 2, 'q', 'registration', 'q', 'q', 'q', 's', 's', 's'),
(3, 'sa', 'proposal1', 2, '573361f088e388174808c92522d8f7c5cd8ef367994ab40da4f913a5efb440f8', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca403430002403a463e74d16be3cae2602699a779901a8cf4b43b499bb09dabad81d84c19f45e96ea73c243fe77e60eaaf99b6813eb6376e53aea336ce88d4c9d4637abe88f80', '[[Ljava.lang.String;@34d65521', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@272d2d6a'),
(4, 'sa', 'proposal1', 2, 'ca3fcbed3eb617e2372f283d98401e19883a05a444d5e2c1726901c21d291b1f', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca403430002403a463e74d16be3cae2602699a779901a8cf4b43b499bb09dabad81d84c19f45e96ea73c243fe77e60eaaf99b6813eb6376e53aea336ce88d4c9d4637abe88f80', '[[Ljava.lang.String;@44bbfb61', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@4f477d91'),
(5, 'sa', 'proposal1', 2, 'b41506243bd900c777906456a8ced6cadadf77a510be1c924fc7a053d75745e8', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca403430002403a463e74d16be3cae2602699a779901a8cf4b43b499bb09dabad81d84c19f45e96ea73c243fe77e60eaaf99b6813eb6376e53aea336ce88d4c9d4637abe88f80', '[[Ljava.lang.String;@578bb34b', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@224cffd3'),
(6, 'sa', 'proposal1', 2, '788a9aae6af08a3ca2ff6081f6886fe101cb42f8bcb57b45622a148459c898d9', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024047b74ed099eedb6e736605346e8cf27d4a7255651560a8d1fcc38339b572f8e2690e9184230b2b7a3cb047437fba9e93a31c959c0635081a3be2024561dd69ec', '[[Ljava.lang.String;@3d0d92e', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@10b23fc'),
(7, 'sa', 'proposal1', 2, 'd1f6e67a4263a5bc2292aa132ac8ccbe008eeec83ca295ab5f44f80c084f3a56', 'registration', '3081f13081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034400024100a042a3349a5b4417c13b7e61ae2aaea61c7bfae7eea4b65ae9e208b8b9ea8ee6521b80515118ef3d77ea18508496c43e9059a2b16c2bbdcfebaa80216ac626b6', '[[Ljava.lang.String;@4de370f4', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@2a342cc1'),
(8, 'sa', 'proposal1', 2, '2025cf38065d5376b9467e852313ee75147aafb6ec8e925757164e52a6ba830f', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@14d43316', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@3be40a1c'),
(9, 'sa', 'proposal1', 2, 'eec3458ee172ced4929989a52dc7be3d711564a9a479d1ae86d7179f4ff2a0a0', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@15f8a718', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@246b46d8'),
(10, 'sa', 'proposal1', 2, 'b8436f475b35c9c213eaae45da9d8b21f48f8074b2a9cce7714b2b37f785a21b', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@2b6fefd6', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@41eb7d2'),
(11, 'sa', 'proposal1', 2, '603c6e96728e173fe945d2952bec65513ae2604edec141cfc4cbd7aad205ad09', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@70743879', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@71995181'),
(12, 'sa', 'proposal1', 2, 'f20b7337669152f3798021f6fb362af2faa4b031895e1905c837e4a4833009e1', 'registration', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@4de370f4', 'sa', 'sa', 'changeOwnership', '[Ljava.lang.Object;@1ce92166'),
(13, 'sa', 'proposal1', 2, '944a0c60c25689de762a385c7f33b0e47687b9d819e7155af908c9fb0b4fee2f', 'ownershipTransfer', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@3a6d07e9', 'sa', 'qq', 'changeOwnership', '[Ljava.lang.Object;@3efeb834'),
(14, 'sa', 'proposal1', 2, '106bacf492e7f85979ac1475dc1a21616a67ae7754a576cf1e9e301e625dd2c4', 'ownershipTransfer', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@23489dfa', 'sa', 'qq', 'changeOwnership', '[Ljava.lang.Object;@2777c66d'),
(15, 'sa', 'proposal1', 2, 'caed76c1f8740a9fdb50e7d0b8e9dba6a9737ec4288d4de001f27ed913d1f077', 'ownershipTransfer', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@59710acb', 'sa', 'qq', 'changeOwnership', '[Ljava.lang.Object;@3efcaea2'),
(16, 'sa', 'proposal1', 2, '4111f11615d36cf430e5cbeaa703816fff81f9977dda6a44d3075120b4cf3a87', 'ownershipTransfer', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@5e279d23', 'sa', 'qq', 'changeOwnership', '[Ljava.lang.Object;@47934c10'),
(17, 'sa', 'proposal1', 2, 'adb5ce129fd15760ea339454de55ee2f9fc20612af3e40fb5d2d44e7c6230186', 'ownershipTransfer', '3081f03081a806072a8648ce38040130819c024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e17021500962eddcc369cba8ebb260ee6b6a126d9346e38c50240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4034300024069c7de7f44246b7519d72c8f7ad01dc3f66891e2951d75bef86de73046081bdc04fcf1deb75e1822bf0db40b41c69efc9fadbfc5f8513afc9b1757d189b77a4f', '[[Ljava.lang.String;@4a12f67', 'sa', 'qq', 'changeOwnership', '[Ljava.lang.Object;@6d765260');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `VehicleHistory`
--
ALTER TABLE `VehicleHistory`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `VehicleHistory`
--
ALTER TABLE `VehicleHistory`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
