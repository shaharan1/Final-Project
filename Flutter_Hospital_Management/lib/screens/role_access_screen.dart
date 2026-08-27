import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/core/role_access.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

class RoleAccessScreen extends ConsumerStatefulWidget {
  const RoleAccessScreen({super.key});

  @override
  ConsumerState<RoleAccessScreen> createState() => _RoleAccessScreenState();
}

class _RoleAccessScreenState extends ConsumerState<RoleAccessScreen> {
  late String _selectedRole;
  final List<String> _roles = defaultRoleAccess.keys.toList();

  @override
  void initState() {
    super.initState();
    _selectedRole = _roles.contains('admin') ? 'admin' : _roles.first;
  }

  String _label(String role) =>
      role[0].toUpperCase() + role.substring(1).toLowerCase();

  @override
  Widget build(BuildContext context) {
    final access = ref.watch(roleAccessProvider);
    final allowed = access[_selectedRole] ??
        defaultRoleAccess[_selectedRole] ??
        <String>{};

    return Scaffold(
      appBar: AppBar(title: const Text('Role Permissions')),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          const Text(
            'Select a role, then toggle which modules it can access. '
            'Changes are saved on this device automatically.',
            style: TextStyle(color: Colors.grey),
          ),
          const SizedBox(height: 14),
          SingleChildScrollView(
            scrollDirection: Axis.horizontal,
            child: Row(
              children: _roles
                  .map((r) => Padding(
                        padding: const EdgeInsets.only(right: 8),
                        child: ChoiceChip(
                          label: Text(_label(r)),
                          selected: _selectedRole == r,
                          onSelected: (_) => setState(() => _selectedRole = r),
                        ),
                      ))
                  .toList(),
            ),
          ),
          const SizedBox(height: 16),
          AppCard(
            child: Column(
              children: allModuleOrder.map((key) {
                final enabled = allowed.contains(key);
                return SwitchListTile(
                  secondary: Icon(moduleIcons[key],
                      color: enabled ? AppTheme.primary : Colors.grey),
                  title: Text(moduleLabels[key] ?? key),
                  value: enabled,
                  onChanged: (_) => ref
                      .read(roleAccessProvider.notifier)
                      .toggle(_selectedRole, key),
                );
              }).toList(),
            ),
          ),
          const SizedBox(height: 16),
          ElevatedButton.icon(
            onPressed: () =>
                ref.read(roleAccessProvider.notifier).resetAll(),
            icon: const Icon(Icons.restart_alt),
            label: const Text('Reset all roles to defaults'),
          ),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
