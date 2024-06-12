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
import {Badge} from "@/components/ui/badge.tsx";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {Input} from "@/components/ui/input.tsx";
import {toast} from "@/components/ui/use-toast.ts";

function UserManagementPage() {
    // @ts-expect-error no time
    const [currentPage, setCurrentPage] = useState(() => parseInt(localStorage.getItem('currentPage')) || 0);
    const [users, setUsers] = useState<ManagedUserType[]>([]);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState<ManagedUserType | null>(null);
    const {t} = useTranslation();
    const {account} = useAccountState();
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [searchPhrase, setSearchPhrase] = useState('');
    const [isSubmitClicked, setIsSubmitClicked] = useState(false);
    const navigator = useNavigate();
    // @ts-expect-error no time
    const [pageSize, setPageSize] = useState(() => parseInt(localStorage.getItem('pageSize')) || 4);
    // @ts-expect-error no time
    const [sortConfig, setSortConfig] = useState(() => JSON.parse(localStorage.getItem('sortConfig')) || {
        key: 'login',
        direction: 'asc'
    });

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
                toast({
                    title: t("accountSettings.popUp.changeUserDataOK.title"),
                    description: selectedUser.blocked ? t("general.userUnblocked") : t("general.userBlocked")
                })
            }).catch((error) => {
                handleApiError(error);
            });
        }
        setAlertDialogOpen(false);
    };

    const fetchUsers = () => {
        const order = sortConfig.direction === 'asc' ? 'true' : 'false';
        const details = `?phrase=${searchPhrase}&orderBy=${sortConfig.key}&order=${order}&pageNumber=${currentPage}&pageSize=${pageSize}`;

        api.getAccountsMatchPhraseInAccount(details)
            .then(response => {
                if (response.status === 204) {
                    setUsers([]);
                } else {
                    const sortedUsers = sortUsers(response.data);
                    setUsers(sortedUsers);
                }
            })
            .catch(error => {
                handleApiError(error);
            });
    };

    // @ts-expect-error no time
    const sortUsers = (users) => {
        const {key, direction} = sortConfig;
        return [...users].sort((a, b) => {
            if (a[key] < b[key]) return direction === 'asc' ? -1 : 1;
            if (a[key] > b[key]) return direction === 'asc' ? 1 : -1;
            return 0;
        });
    };

    useEffect(() => {
        fetchUsers();
        setIsSubmitClicked(false);
    }, [currentPage, pageSize, sortConfig, isSubmitClicked]);

    useEffect(() => {
        localStorage.setItem('currentPage', currentPage.toString());
        localStorage.setItem('pageSize', pageSize.toString());
        localStorage.setItem('sortConfig', JSON.stringify(sortConfig));
    }, [currentPage, pageSize, sortConfig]);

    // @ts-expect-error no time
    const handleSortChange = (value) => {
        const [key, direction] = value.split('-');
        setSortConfig({key, direction});
    };

    // @ts-expect-error no time
    const handlePageSizeChange = (value) => {
        setPageSize(parseInt(value));
        setCurrentPage(0);
    };

    function refresh() {
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
            <div className="flex justify-items-start p-2 mt-4">
                <Select onValueChange={handlePageSizeChange} value={pageSize.toString()}>
                    <SelectTrigger className={"w-auto mr-4"}>
                        <SelectValue placeholder={t("general.itemsPerPage")}/>
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="2">2</SelectItem>
                        <SelectItem value="4">4</SelectItem>
                        <SelectItem value="6">6</SelectItem>
                        <SelectItem value="8">8</SelectItem>
                        <SelectItem value="10">10</SelectItem>
                    </SelectContent>
                </Select>
                <Select onValueChange={handleSortChange} value={`${sortConfig.key}-${sortConfig.direction}`}>
                    <SelectTrigger className={"w-1/4 mr-4 flex"}>
                        <SelectValue placeholder={t("general.sortBy")}/>
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem
                            value="login-asc">{t("accountSettings.users.table.header.login")} {t("general.asc")}</SelectItem>
                        <SelectItem
                            value="login-desc">{t("accountSettings.users.table.header.login")} {t("general.desc")}</SelectItem>
                        <SelectItem
                            value="level-asc">{t("accountSettings.users.table.header.userLevels")} {t("general.asc")}</SelectItem>
                        <SelectItem
                            value="level-desc">{t("accountSettings.users.table.header.userLevels")} {t("general.desc")}</SelectItem>
                    </SelectContent>
                </Select>
                <Input
                    value={searchPhrase}
                    onChange={(e) => setSearchPhrase(e.target.value)}
                    placeholder={t("general.search")}
                    className="mr-4 w-2/3"
                />
                <Button onClick={() => setIsSubmitClicked(true)}>{t("general.filter")}</Button>
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
                            <TableRow key={user.id} className="flex-auto">
                                <TableCell>{user.login}</TableCell>
                                <TableCell>{user.name}</TableCell>
                                <TableCell>{user.lastName}</TableCell>
                                <TableCell className="flex-col justify-center">
                                    {user.active ? <FiCheck color="green"/> : <FiX color="red"/>}
                                </TableCell>
                                <TableCell className="flex-col justify-center">
                                    {user.blocked ? <FiCheck color="red"/> : <FiX color="green"/>}
                                </TableCell>
                                <TableCell className="flex-auto">
                                    {user.suspended ? <FiCheck color="red"/> : <FiX color="green"/>}
                                </TableCell>
                                <TableCell>
                                    {user.userLevels.map(level => {
                                        return <Badge key={level} variant={"secondary"}>{level} </Badge>;
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
                <div className={"pt-5"}>
                    <Pagination>
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
                                        if (users.length === pageSize) setCurrentPage(currentPage + 1)
                                    }}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                </div>
            </div>
        </div>
    );
}

export default UserManagementPage;
