import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { type User } from '../../models/user.model';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-dashboard',
  imports: [FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private userService = inject(UserService);

  users = signal<User[]>([]);
  availableRoles = [
    {id: 1, roleName: 'ADMIN'},
    {id: 2, roleName: 'TRAINER'},
    {id: 3, roleName: 'USER'},
  ];

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe({
      next: (users) => {
        console.log('Backend users response:', JSON.stringify(users[0]));
        this.users.set(users);
      },
      error: (err) => console.error('Felhasználók betöltése sikertelen:', err),
    });
  }

  onRoleChange(user: User, roleId: number) {
    this.userService.updateUserRole(user.id, roleId).subscribe({
      next: () => {
/*         user.role.id = roleId; */
        console.log(`${user.email} role módosítva: ${roleId}`);
      },
      error: (err) => console.error('Role módosítás sikertelen:', err, user.id, roleId),
    });
  }

  deleteUser(user: User) {
    if (!confirm(`Biztosan törölni szeretnéd: ${user.firstName} ${user.lastName} (${user.email})?`)) return;

    this.userService.deleteUser(user.email).subscribe({
      next: () => {
        this.users.update(users => users.filter(u => u.email !== user.email));
        console.log(`${user.email} törölve`);
      },
      error: (err) => console.error('Törlés sikertelen:', err),
    });
  }
}
