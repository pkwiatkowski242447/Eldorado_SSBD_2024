import {useEffect, useState} from 'react';
import SiteHeader from "@/components/SiteHeader.tsx";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {api} from "@/api/api.ts";
import {ManagedUserType} from "@/types/Users.ts";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {useNavigate} from "react-router-dom";
import {FiSettings} from 'react-icons/fi';
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {toast} from "@/components/ui/use-toast.ts";
import {useAccountState} from "@/context/AccountContext.tsx";

function UserManagementPage() {
    const [currentPage, setCurrentPage] = useState(0);
    const [users, setUsers] = useState<ManagedUserType[]>([]);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState<ManagedUserType | null>(null);

    const {account} = useAccountState();

    const navigator = useNavigate();

    const handleSettingsClick = (userId: string) => {
        navigator(`/manage-users/${userId}`);
    };

    const handleBlockUnblockClick = (user: ManagedUserType) => {
        setSelectedUser(user);
        setAlertDialogOpen(true);
    };

    const handleConfirmBlockUnblock = () => {
        if (selectedUser) {
            if (selectedUser.blocked) {
                console.log('unblock')
            } else {
                api.blockAccount(selectedUser.id).then(() => {
                    api.getAccounts(`?pageNumber=${currentPage}&pageSize=4`).then(response => {
                        if (response.status === 204) {
                            setUsers([]);
                        } else {
                            setUsers(response.data);
                        }
                    });
                    setAlertDialogOpen(false);
                }).catch(error => {
                    toast({
                        variant: "destructive",
                        description: "Something went wrong. Please try again later.",
                    })
                    console.table(error);
                });
            }
        }
        setAlertDialogOpen(false);
    };

    useEffect(() => {
        api.getAccounts(`?pageNumber=${currentPage}&pageSize=4`).then(response => {
            if (response.status === 204) {
                setUsers([]);
            } else {
                setUsers(response.data);
            }
        });
    }, [currentPage]);

    return (
        <div className="flex min-h-screen w-full flex-col">
            <SiteHeader/>
            <Table className="p-10">
                <TableHeader>
                    <TableRow>
                        <TableHead>Login</TableHead>
                        <TableHead>Name</TableHead>
                        <TableHead>Lastname</TableHead>
                        <TableHead>Active</TableHead>
                        <TableHead>Blocked</TableHead>
                        <TableHead>Verified</TableHead>
                        <TableHead>User Levels</TableHead>
                        <TableHead></TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {users.map(user => (
                        <TableRow key={user.id}>
                            <TableCell>{user.login}</TableCell>
                            <TableCell>{user.name}</TableCell>
                            <TableCell>{user.lastName}</TableCell>
                            <TableCell>{user.active.toString()}</TableCell>
                            <TableCell>{user.blocked.toString()}</TableCell>
                            <TableCell>{user.verified.toString()}</TableCell>
                            <TableCell>
                                {user.userLevels.map(level => {
                                    let color;
                                    switch (level) {
                                        case 'Admin':
                                            color = 'red';
                                            break;
                                        case 'Staff':
                                            color = 'blue';
                                            break;
                                        case 'Client':
                                            color = 'green';
                                            break;
                                        default:
                                            color = 'black';
                                    }
                                    return <span style={{color}}>{level}</span>;
                                })}
                            </TableCell>
                            <TableCell>
                                <DropdownMenu>
                                    <DropdownMenuTrigger>
                                        <FiSettings/>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent>
                                        <DropdownMenuItem onClick={() => handleSettingsClick(user.id)}>
                                            Manage
                                        </DropdownMenuItem>
                                        <DropdownMenuItem
                                            onClick={() => handleBlockUnblockClick(user)}
                                            disabled={user.id === account?.id}
                                        >
                                            {user.blocked ? 'Unblock' : 'Block'}
                                        </DropdownMenuItem>
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
                <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                    <AlertDialogContent>
                        <AlertDialogTitle>Confirm</AlertDialogTitle>
                        <AlertDialogDescription>
                            Are you sure you want to {selectedUser?.blocked ? 'unblock' : 'block'} this user?
                        </AlertDialogDescription>
                        <AlertDialogAction onClick={handleConfirmBlockUnblock}>OK</AlertDialogAction>
                        <AlertDialogCancel>Cancel</AlertDialogCancel>
                    </AlertDialogContent>
                </AlertDialog>
            </Table>
            <Pagination>
                <PaginationContent>
                    <PaginationItem>
                        <PaginationPrevious onClick={() => {
                            if (currentPage > 0) setCurrentPage(currentPage - 1)
                        }}/>
                    </PaginationItem>
                    <PaginationItem>
                        <PaginationLink>{currentPage + 1}</PaginationLink>
                    </PaginationItem>
                    <PaginationItem>
                        <PaginationNext onClick={() => {
                            if (users.length > 0) setCurrentPage(currentPage + 1)
                        }}/>
                    </PaginationItem>
                </PaginationContent>
            </Pagination>
        </div>
    );
}

export default UserManagementPage;