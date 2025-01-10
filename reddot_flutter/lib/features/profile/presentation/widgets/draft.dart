// import 'package:flutter/material.dart';
// import '../../../../resources/contants/constants.dart';

// class HomePage extends StatefulWidget {
//   const HomePage({super.key});
//   @override
//   State<HomePage> createState() => _HomePageState();
// }

// class _HomePageState extends State<HomePage>
//     with SingleTickerProviderStateMixin {
//   late TabController tabController;
//   @override
//   void initState() {
//     tabController = TabController(length: 4, vsync: this);
//     tabController.addListener(() {
//       setState(() {});
//     });
//     super.initState();
//   }

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         centerTitle: true,
//         //tabcontroller.index can be used to get the name of current index value of the tabview.
//         title: Text(tabController.index == 0
//             ? TextConstants.titleTab_1
//             : tabController.index == 1
//                 ? TextConstants.titleTab_2
//                 : tabController.index == 2
//                     ? TextConstants.titleTab_3
//                     : TextConstants.titleTab_4),
//         bottom: TabBar(controller: tabController, tabs: [
//           Tab(
//             text: TextConstants.titleTab_1,
//             icon: Icon(
//               Icons.signpost,
//               color: Colors.indigo.shade500,
//             ),
//           ),
//           Tab(
//               text: TextConstants.titleTab_2,
//               icon: Icon(
//                 Icons.signpost,
//                 color: Colors.indigo.shade500,
//               )),
//           Tab(
//               text: TextConstants.titleTab_3,
//               icon: Icon(
//                 Icons.signpost,
//                 color: Colors.indigo.shade500,
//               )),
//           Tab(
//               text: TextConstants.titleTab_4,
//               icon: Icon(
//                 Icons.signpost,
//                 color: Colors.indigo.shade500,
//               ))
//         ]),
//       ),
//       body: TabBarView(
//         controller: tabController,
//         children: [
//           FirstScreen(
//             tabController: tabController,
//             title: TextConstants.titleTab_1,
//           ),
//           FirstScreen(
//             tabController: tabController,
//             title: TextConstants.titleTab_2,
//           ),
//           FirstScreen(
//             tabController: tabController,
//             title: TextConstants.titleTab_3,
//           ),
//           FirstScreen(
//             tabController: tabController,
//             title: TextConstants.titleTab_4,
//           )
//         ],
//       ),
//     );
//   }
// }

// class FirstScreen extends StatelessWidget {
//   final TabController tabController;
//   final String title;

//   const FirstScreen(
//       {super.key, required this.tabController, required this.title});
//   @override
//   Widget build(BuildContext context) {
//     return SingleChildScrollView(
//       child: Column(
//         children: [
//           const SizedBox(height: 20),
//           Text(
//             title,
//             style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
//           ),
//           const SizedBox(height: 20),
//           ListView.builder(
//             shrinkWrap: true,
//             itemCount: 10,
//             itemBuilder: (context, index) {
//               return ListTile(
//                 title: Text('Item $index'),
//                 subtitle: Text('Subtitle $index'),
//                 leading: const Icon(Icons.signpost),
//                 trailing: const Icon(Icons.arrow_forward_ios),
//               );
//             },
//           ),
//         ],
//       ),
//     );
//   }
// }
