import 'package:flutter/material.dart';
import 'package:flutter_hospital_management/theme.dart';

class AppCard extends StatelessWidget {
  final Widget child;
  final EdgeInsetsGeometry padding;
  final VoidCallback? onTap;

  const AppCard({
    super.key,
    required this.child,
    this.padding = const EdgeInsets.all(16),
    this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(16),
        child: Padding(padding: padding, child: child),
      ),
    );
  }
}

class SectionTitle extends StatelessWidget {
  final String title;
  final IconData? icon;

  const SectionTitle(this.title, {super.key, this.icon});

  @override
  Widget build(BuildContext context) => Padding(
        padding: const EdgeInsets.only(bottom: 12, top: 4),
        child: Row(
          children: [
            if (icon != null) ...[
              Icon(icon, color: AppTheme.primary, size: 20),
              const SizedBox(width: 8),
            ],
            Text(
              title,
              style: const TextStyle(
                fontSize: 17,
                fontWeight: FontWeight.bold,
                color: Color(0xFF263238),
              ),
            ),
          ],
        ),
      );
}

class StatusChip extends StatelessWidget {
  final String label;
  final Color color;

  const StatusChip(this.label, this.color, {super.key});

  factory StatusChip.fromStatus(String status) {
    final s = status.toUpperCase();
    Color c;
    if (s.contains('PAID') || s.contains('COMPLETED') || s.contains('ACTIVE')) {
      c = AppTheme.success;
    } else if (s.contains('PEND') || s.contains('PARTIAL')) {
      c = AppTheme.warning;
    } else if (s.contains('FAIL') || s.contains('CANCEL') || s.contains('UNPAID')) {
      c = AppTheme.danger;
    } else {
      c = AppTheme.info;
    }
    return StatusChip(status, c);
  }

  @override
  Widget build(BuildContext context) => Container(
        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
        decoration: BoxDecoration(
          color: color.withValues(alpha: 0.12),
          borderRadius: BorderRadius.circular(20),
          border: Border.all(color: color.withValues(alpha: 0.35)),
        ),
        child: Text(
          label,
          style: TextStyle(
            color: color,
            fontSize: 12,
            fontWeight: FontWeight.w600,
          ),
        ),
      );
}

class EmptyState extends StatelessWidget {
  final String message;
  final IconData icon;

  const EmptyState(this.message, {super.key, this.icon = Icons.inbox});

  @override
  Widget build(BuildContext context) => Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(icon, size: 56, color: Colors.grey.shade400),
            const SizedBox(height: 12),
            Text(
              message,
              style: TextStyle(color: Colors.grey.shade600),
              textAlign: TextAlign.center,
            ),
          ],
        ),
      );
}

class AnimatedCounter extends StatefulWidget {
  final double value;
  final String prefix;
  final String Function(double) format;

  const AnimatedCounter(this.value,
      {super.key, this.prefix = '', this.format = _defaultFormat});

  static String _defaultFormat(double v) =>
      v % 1 == 0 ? v.toInt().toString() : v.toStringAsFixed(2);

  @override
  State<AnimatedCounter> createState() => _AnimatedCounterState();
}

class _AnimatedCounterState extends State<AnimatedCounter>
    with SingleTickerProviderStateMixin {
  late AnimationController _ctrl;
  late Animation<double> _anim;

  @override
  void initState() {
    super.initState();
    _ctrl = AnimationController(
        vsync: this, duration: const Duration(milliseconds: 900));
    _anim = Tween<double>(begin: 0, end: widget.value).animate(
        CurvedAnimation(parent: _ctrl, curve: Curves.easeOutCubic));
    _ctrl.forward();
  }

  @override
  void didUpdateWidget(covariant AnimatedCounter old) {
    super.didUpdateWidget(old);
    if (old.value != widget.value) {
      _anim = Tween<double>(begin: _anim.value, end: widget.value).animate(
          CurvedAnimation(parent: _ctrl, curve: Curves.easeOutCubic));
      _ctrl.forward(from: 0);
    }
  }

  @override
  void dispose() {
    _ctrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => AnimatedBuilder(
        animation: _anim,
        builder: (_, _) =>
            Text('${widget.prefix}${widget.format(_anim.value)}'),
      );
}

class MiniBar extends StatelessWidget {
  final String label;
  final double value;
  final double max;
  final Color color;

  const MiniBar(this.label, this.value, this.max, this.color, {super.key});

  @override
  Widget build(BuildContext context) => Padding(
        padding: const EdgeInsets.symmetric(vertical: 6),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                    child: Text(label,
                        overflow: TextOverflow.ellipsis,
                        style: const TextStyle(fontSize: 13))),
                Text(value.toStringAsFixed(2),
                    style: const TextStyle(
                        fontSize: 13, fontWeight: FontWeight.w600)),
              ],
            ),
            const SizedBox(height: 6),
            TweenAnimationBuilder<double>(
              duration: const Duration(milliseconds: 800),
              curve: Curves.easeOutCubic,
              tween: Tween(begin: 0, end: max > 0 ? value / max : 0),
              builder: (_, v, _) => LinearProgressIndicator(
                value: v,
                minHeight: 8,
                backgroundColor: color.withValues(alpha: 0.12),
                valueColor: AlwaysStoppedAnimation(color),
                borderRadius: BorderRadius.circular(8),
              ),
            ),
          ],
        ),
      );
}

class GradientHeader extends StatelessWidget {
  final String title;
  final String? subtitle;

  const GradientHeader(this.title, {super.key, this.subtitle});

  @override
  Widget build(BuildContext context) => Container(
        width: double.infinity,
        decoration: const BoxDecoration(gradient: AppTheme.primaryGradient),
        padding: const EdgeInsets.fromLTRB(20, 18, 20, 22),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(title,
                style: const TextStyle(
                    color: Colors.white,
                    fontSize: 22,
                    fontWeight: FontWeight.bold)),
            if (subtitle != null) ...[
              const SizedBox(height: 4),
              Text(subtitle!,
                  style: const TextStyle(color: Colors.white70, fontSize: 13)),
            ],
          ],
        ),
      );
}
