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

function UserManagementPage() {
    const [currentPage, setCurrentPage] = useState(0);
    const [users, setUsers] = useState<ManagedUserType[]>([]);

    const navigator = useNavigate();

    const handleSettingsClick = (userId: string) => {
        navigator(`/manage-users/${userId}`);
    };

    const handleBlockUnblockClick = (userId: string) => {
        console.log(userId)
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
                        <TableHead>Actions</TableHead>
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
                                        Manage
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent>
                                        <DropdownMenuItem onClick={() => handleSettingsClick(user.id)}>
                                            Settings
                                        </DropdownMenuItem>
                                        <DropdownMenuItem onClick={() => handleBlockUnblockClick(user.id)}>
                                            Block/Unblock
                                        </DropdownMenuItem>
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
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