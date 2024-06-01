// private static void userRegistration(Scanner scanner, ArrayList<IceCream> daftarIceCream, ArrayList<User> daftarUser) {
//         System.out.print("Masukkan Username: ");
//         String username = scanner.nextLine();
//         System.out.print("Masukkan Nomor Hp: ");
//         String nomor_hp = scanner.nextLine();
//         System.out.print("Masukkan Email: ");
//         String email = scanner.nextLine();
//         System.out.print("Masukkan Jenis Kelamin (L/P): ");
//         String jenis_kelamin = scanner.nextLine();
//         System.out.print("Masukkan Alamat: ");
//         String alamat = scanner.nextLine();
//         System.out.print("Masukkan Password: ");
//         String password = scanner.nextLine();
//         System.out.print("Konfirmasi Password: ");
//         String confirmPassword = scanner.nextLine();

//         if (isUsernameTaken(daftarUser, username)) {
//             System.out.println("Username Sudah Digunakan. Silakan Gunakan Username Lain.");
//             return;
//         }

//         if (!password.equals(confirmPassword)) {
//             System.out.println("Konfirmasi Password Salah! Registrasi Gagal.");
//             return;
//         }

//         User newUser = new User(username, password, nomor_hp, email, jenis_kelamin, alamat, new ArrayList<>());
//         daftarUser.add(newUser);

//         // Registrasi user ke database
//         registerUser(newUser);

//         System.out.println("Registrasi Berhasil!");
//     }