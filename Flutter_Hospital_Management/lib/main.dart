import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/providers/auth_provider.dart';
import 'package:flutter_hospital_management/screens/login_screen.dart';
import 'package:flutter_hospital_management/screens/home_screen.dart';
import 'package:flutter_hospital_management/theme.dart';

void main() {
  runApp(const ProviderScope(child: MyApp()));
}

class MyApp extends ConsumerStatefulWidget {
  const MyApp({super.key});

  @override
  ConsumerState<MyApp> createState() => _MyAppState();
}

class _MyAppState extends ConsumerState<MyApp> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
        () => ref.read(authNotifierProvider.notifier).tryAutoLogin());
  }

  @override
  Widget build(BuildContext context) {
    final auth = ref.watch(authNotifierProvider);

    Widget home;
    if (auth.isLoading && auth.token == null) {
      home = const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    } else if (auth.token != null) {
      home = const HomeScreen();
    } else {
      home = const LoginScreen();
    }

    return MaterialApp(
      title: 'Elite Care Hospital',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.light,
      home: home,
    );
  }
}
