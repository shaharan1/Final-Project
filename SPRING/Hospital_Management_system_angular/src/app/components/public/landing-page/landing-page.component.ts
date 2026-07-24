import { CommonModule } from '@angular/common';
import { Component, HostListener } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css',
})
export class LandingPageComponent {
  menuOpen = false;
  isScrolled = false;

  @HostListener('window:scroll')
  onScroll() {
    this.isScrolled = window.scrollY > 40;
  }

  stats = [
    { value: '25+', label: 'Years Experience' },
    { value: '150+', label: 'Expert Doctors' },
    { value: '500+', label: 'Beds Available' },
    { value: '50K+', label: 'Patients Treated' },
  ];

  services = [
    { icon: '🩺', title: 'Appointment Booking', desc: 'Schedule visits with top doctors instantly.' },
    { icon: '🚑', title: 'Emergency Care', desc: '24/7 emergency medical support.' },
    { icon: '🚐', title: 'Ambulance Service', desc: 'Fast ambulance dispatch anytime.' },
    { icon: '💊', title: 'Pharmacy', desc: 'In-house pharmacy with all medicines.' },
    { icon: '🔬', title: 'Laboratory', desc: 'Advanced diagnostic lab facilities.' },
    { icon: '🩻', title: 'Health Checkup', desc: 'Comprehensive health packages.' },
  ];

  departments = [
    { icon: '❤️', name: 'Cardiology', desc: 'Heart care & cardiovascular surgery.' },
    { icon: '🧠', name: 'Neurology', desc: 'Brain & nervous system treatment.' },
    { icon: '🦴', name: 'Orthopedics', desc: 'Bone, joint & spine care.' },
    { icon: '👶', name: 'Pediatrics', desc: 'Child healthcare & vaccination.' },
    { icon: '🤰', name: 'Gynecology', desc: 'Women health & maternity care.' },
    { icon: '🏥', name: 'ICU', desc: 'Critical care & monitoring.' },
    { icon: '🚑', name: 'Emergency', desc: '24/7 emergency medical services.' },
    { icon: '🩺', name: 'General Medicine', desc: 'Primary care & internal medicine.' },
  ];

  doctors = [
    { name: 'Dr. Ahsan Rahman', spec: 'Cardiologist', exp: '15 Years', qual: 'MBBS, MD' },
    { name: 'Dr. Fatima Khan', spec: 'Neurologist', exp: '12 Years', qual: 'MBBS, DM' },
    { name: 'Dr. Kamal Hossain', spec: 'Orthopedic Surgeon', exp: '18 Years', qual: 'MBBS, MS' },
    { name: 'Dr. Nasrin Akter', spec: 'Pediatrician', exp: '10 Years', qual: 'MBBS, DCH' },
    { name: 'Dr. Rafiq Ahmed', spec: 'General Surgeon', exp: '20 Years', qual: 'MBBS, FRCS' },
    { name: 'Dr. Sabrina Islam', spec: 'Gynecologist', exp: '14 Years', qual: 'MBBS, FCPS' },
  ];

  whyChooseUs = [
    { icon: '👨‍⚕️', title: 'Experienced Doctors', desc: 'Board-certified specialists with years of experience.' },
    { icon: '🚑', title: '24/7 Emergency', desc: 'Round-the-clock emergency medical services.' },
    { icon: '🔬', title: 'Modern Equipment', desc: 'State-of-the-art medical technology.' },
    { icon: '📱', title: 'Online Appointment', desc: 'Book appointments from anywhere.' },
    { icon: '📋', title: 'Digital Reports', desc: 'Access lab reports & records online.' },
    { icon: '💰', title: 'Affordable Treatment', desc: 'Quality healthcare at reasonable cost.' },
  ];
}
