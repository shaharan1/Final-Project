import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_hospital_management/models/doctor.dart';
import 'package:flutter_hospital_management/widgets/app_drawer.dart';
import 'package:flutter_hospital_management/widgets/common.dart';
import 'package:flutter_hospital_management/theme.dart';

class DoctorDetailScreen extends ConsumerStatefulWidget {
  final Doctor item;
  const DoctorDetailScreen({super.key, required this.item});

  @override
  ConsumerState<DoctorDetailScreen> createState() => _DoctorDetailScreenState();
}

class _DoctorDetailScreenState extends ConsumerState<DoctorDetailScreen> {
  late Doctor _m;

  @override
  void initState() {
    super.initState();
    _m = widget.item;
  }

  @override
  Widget build(BuildContext context) {
    final m = _m;
    return Scaffold(
      appBar: AppBar(title: Text(m.displayName)),
      drawer: const AppDrawer(),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Center(
            child: CircleAvatar(
              radius: 38,
              backgroundColor: AppTheme.primary.withValues(alpha: 0.12),
              child: const Icon(Icons.medical_services,
                  size: 40, color: AppTheme.primary),
            ),
          ),
          const SizedBox(height: 10),
          Center(
            child: Text(m.displayName,
                style: const TextStyle(
                    fontSize: 20, fontWeight: FontWeight.bold)),
          ),
          const SizedBox(height: 4),
          if (m.specialization != null)
            Center(
              child: Text(m.specialization!,
                  style: const TextStyle(color: Colors.grey)),
            ),
          const SizedBox(height: 20),
          DetailSection('Professional Information', [
            DetailRow('Designation', m.designation, icon: Icons.work),
            DetailRow('Department', m.departmentName, icon: Icons.business),
            DetailRow('Specialization', m.specialization,
                icon: Icons.local_hospital),
            DetailRow('Study', m.study, icon: Icons.school),
            DetailRow('Registration No.', m.registrationNumber,
                icon: Icons.badge),
            DetailRow('Experience', m.experienceYears?.toString(),
                icon: Icons.timer),
          ], icon: Icons.medical_services),
          DetailSection('Availability', [
            DetailRow('Available Days', m.availableDays,
                icon: Icons.calendar_today),
            DetailRow('Duty Hours', m.dutyHours, icon: Icons.schedule),
            DetailRow('Chamber', m.chamber, icon: Icons.meeting_room),
            DetailRow('Gender', m.gender, icon: Icons.wc),
            DetailRow('Status', m.status, icon: Icons.info),
          ], icon: Icons.event_available),
          DetailSection('Fees', [
            DetailRow('Consultation Fee', m.consultationFee?.toString(),
                icon: Icons.currency_rupee),
            DetailRow('Follow-up Fee', m.followUpFee?.toString(),
                icon: Icons.currency_rupee),
          ], icon: Icons.payment),
          DetailSection('Contact', [
            DetailRow('Phone', m.phone, icon: Icons.phone),
            DetailRow('Email', m.email, icon: Icons.email),
          ], icon: Icons.contact_phone),
          if (m.description != null)
            DetailSection('About', [
              DetailRow('Description', m.description, icon: Icons.description),
            ], icon: Icons.notes),
          const SizedBox(height: 8),
        ],
      ),
    );
  }
}
