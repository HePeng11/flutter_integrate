import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:webview_flutter/webview_flutter.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static String filePath = 'static/index.html';
  static String jsPath = 'static/script.js';

  WebViewController controller = WebViewController()
    ..setJavaScriptMode(JavaScriptMode.unrestricted)
    ..setBackgroundColor(const Color(0x00000000))
    ..loadFlutterAsset(filePath)
    ..runJavaScript(jsPath);

  @override
  Widget build(BuildContext context) {
    rootBundle
        .loadString(jsPath)
        .then((value) => controller.runJavaScript(value));

    controller.addJavaScriptChannel(
      "getData",
      onMessageReceived: (msg) {
        String mes = msg.message;
        debugPrint('Flutter got js request { $mes } ');
        rootBundle.loadString(msg.message).then(
            (value) => {controller.runJavaScript('setFlutterData($value)')});
      },
    );

    controller.addJavaScriptChannel(
      "giveback",
      onMessageReceived: (msg) {},
    );

    controller.addJavaScriptChannel(
      "callNative",
      onMessageReceived: (msg) {
        // callAndroid();
        controller.runJavaScript('callNativeBack()');
      },
    );

    const mc = MethodChannel("toFlutterChannelName");
    mc.setMethodCallHandler(flutterMethod);

    return Scaffold(
      appBar: AppBar(
          title: const Text(
        'PE POC',
        style: TextStyle(
          fontSize: 24.0,
          fontWeight: FontWeight.bold,
          color: Colors.green,
        ),
      )),
      body:
          // const Text( 'PE POC')
          WebViewWidget(controller: controller),
    );
  }

  Future<void> callAndroid() async {
    const nativeName = "flutterUseJava";
    const platform = MethodChannel(nativeName);
    String returnValue = await platform.invokeMethod("this is flutter");
    debugPrint(" $nativeName response is：$returnValue");
  }

  Future<dynamic> flutterMethod(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'flutterMethod':
        debugPrint(
            'Android called flutterMethod success, and argument is 12345 ${methodCall.arguments}');
        Future<Object> s = controller.runJavaScriptReturningResult(
            'callByNative("${methodCall.arguments}")');
        s.then((value) => {debugPrint('callByNative success , can call native code now : $value')});
        return "flutterMethod success";
      default:
        debugPrint(
            'Android called flutterMethod failed, and argument is ${methodCall.arguments}');
    }
  }
}
