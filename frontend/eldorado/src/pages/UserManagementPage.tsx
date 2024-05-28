import {useEffect, useState} from 'react';
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
import {FiCheck, FiSettings, FiX} from 'react-icons/fi';
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from '@/components/ui/breadcrumb.tsx';
import {useAccountState} from "@/context/AccountContext.tsx";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {Loader2, Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";

function UserManagementPage() {
    const [currentPage, setCurrentPage] = useState(0);
    const [users, setUsers] = useState<ManagedUserType[]>([]);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState<ManagedUserType | null>(null);
    const {t} = useTranslation();
    const {account} = useAccountState();
    const [isRefreshing, setIsRefreshing] = useState(false);
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
            const apiCall = selectedUser.blocked ? api.unblockAccount : api.blockAccount;
            apiCall(selectedUser.id).then(() => {
                fetchUsers();
                setAlertDialogOpen(false);
            }).catch((error) => {
                handleApiError(error);
            });
        }
        setAlertDialogOpen(false);
    };

    const fetchUsers = () => {
        api.getAccounts(`?pageNumber=${currentPage}&pageSize=4`).then(response => {
            if (response.status === 204) {
                setUsers([]);
            } else {
                setUsers(response.data);
            }
        });
    };

    useEffect(() => {
        fetchUsers();
    }, [currentPage]);

    function refresh () {
        setIsRefreshing(true);
        fetchUsers();
        setTimeout(() => setIsRefreshing(false), 1000);
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
            <Breadcrumb className={"pl-2"}>
                <BreadcrumbList>
                    <BreadcrumbItem>
                        <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                    </BreadcrumbItem>
                    <BreadcrumbSeparator>
                        <Slash/>
                    </BreadcrumbSeparator>
                    <BreadcrumbItem>
                        <BreadcrumbLink>{t("breadcrumb.manageUsers")}</BreadcrumbLink>
                    </BreadcrumbItem>
                </BreadcrumbList>
            </Breadcrumb>
                <Button onClick={refresh} variant={"ghost"} className="w-auto" disabled={isRefreshing}>
                    {isRefreshing ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("general.refresh")
                    )}
                </Button>
            </div>
            <div className={"pt-5"}>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("accountSettings.users.table.header.login")}</TableHead>
                            <TableHead
                                className="text-center">{t("accountSettings.users.table.header.name")}</TableHead>
                            <TableHead
                                className="text-center">{t("accountSettings.users.table.header.lastName")}</TableHead>
                            <TableHead
                                className="text-center">{t("accountSettings.users.table.header.active")}</TableHead>
                            <TableHead
                                className="text-center">{t("accountSettings.users.table.header.blocked")}</TableHead>
                            <TableHead
                                className="text-center">{t("accountSettings.users.table.header.suspended")}</TableHead>
                            <TableHead
                                className="text-center">{t("accountSettings.users.table.header.userLevels")}</TableHead>
                            <TableHead className="text-center"></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {users.map(user => (
                            <TableRow key={user.id} className="text-center">
                                <TableCell>{user.login}</TableCell>
                                <TableCell>{user.name}</TableCell>
                                <TableCell>{user.lastName}</TableCell>
                                <TableCell className="text-center">
                                    {user.active ? <FiCheck color="green"/> : <FiX color="red"/>}
                                </TableCell>
                                <TableCell className="text-center">
                                    {user.blocked ? <FiCheck color="green"/> : <FiX color="red"/>}
                                </TableCell>
                                <TableCell>
                                    {user.verified ? <FiCheck color="green"/> : <FiX color="red"/>}
                                </TableCell>
                                <TableCell>
                                    {user.userLevels.map(level => {
                                        let color;
                                        switch (level) {
                                            case 'Admin':
                                                color = 'black';
                                                break;
                                            case 'Staff':
                                                color = 'black';
                                                break;
                                            case 'Client':
                                                color = 'black';
                                                break;
                                            default:
                                                color = 'black';
                                        }
                                        return <span style={{color}}>{level} </span>;
                                    })}
                                </TableCell>
                                <TableCell>
                                    <DropdownMenu>
                                        <DropdownMenuTrigger>
                                            <FiSettings/>
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent>
                                            <DropdownMenuItem onClick={() => handleSettingsClick(user.id)}>
                                                {t("accountSettings.users.table.settings.manage")}
                                            </DropdownMenuItem>
                                            <DropdownMenuItem
                                                onClick={() => handleBlockUnblockClick(user)}
                                                disabled={user.id === account?.id}
                                            >
                                                {user.blocked ? t("accountSettings.users.table.settings.unblock") : t("accountSettings.users.table.settings.block")}
                                            </DropdownMenuItem>
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                    <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                        <AlertDialogContent>
                            <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                            <AlertDialogDescription>
                                {t("accountSettings.users.table.settings.block.confirm1")}
                                {selectedUser?.blocked ? t("accountSettings.users.table.settings.unblock2") : t("accountSettings.users.table.settings.block2")}
                                {t("accountSettings.users.table.settings.block.confirm2")}
                            </AlertDialogDescription>
                            <AlertDialogAction onClick={handleConfirmBlockUnblock}>{t("general.ok")}</AlertDialogAction>
                            <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                        </AlertDialogContent>
                    </AlertDialog>
                </Table>
            </div>
            <Pagination className="sticky bottom-0 w-full bg-white p-4">
                <PaginationContent>
                    <PaginationItem>
                        <PaginationPrevious
                            onClick={() => {
                                if (currentPage > 0) setCurrentPage(currentPage - 1)
                            }}
                        />
                    </PaginationItem>
                    <PaginationItem>
                        <PaginationLink>{currentPage + 1}</PaginationLink>
                    </PaginationItem>
                    <PaginationItem>
                        <PaginationNext
                            onClick={() => {
                                if (users.length > 0) setCurrentPage(currentPage + 1)
                            }}
                        />
                    </PaginationItem>
                </PaginationContent>
            </Pagination>
        </div>
    );
}

export default UserManagementPage;
