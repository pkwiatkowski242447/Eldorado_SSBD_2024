import {useEffect, useState} from 'react';
import {api} from "@/api/api.ts";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from '@/components/ui/breadcrumb.tsx';
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
// import {
//     DropdownMenu,
//     DropdownMenuContent,
//     DropdownMenuItem,
//     DropdownMenuTrigger
// } from "@/components/ui/dropdown-menu.tsx";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {Badge} from "@/components/ui/badge.tsx";
import {Loader2, Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {ParkingListType} from "@/types/Parking.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
// import {FiSettings} from 'react-icons/fi';

function ParkingManagementPage() {
    // @ts-expect-error no time
    const [currentPage, setCurrentPage] = useState(() => parseInt(0));
    const [parking, setParking] = useState<ParkingListType[]>([]);
    // const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    // const [selectedUser, setSelectedUser] = useState<ManagedUserType | null>(null);
    const {t} = useTranslation();
    // const {account} = useAccountState();
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [isSubmitClicked, setIsSubmitClicked] = useState(false);
    // const navigator = useNavigate();
    const pageSize = 5;

    // const handleSettingsClick = (userId: string) => {
    //     navigator(`/manage-users/${userId}`);
    // };

    // const handleBlockUnblockClick = (user: ManagedUserType) => {
    //     setSelectedUser(user);
    //     setAlertDialogOpen(true);
    // };

    // const handleConfirmBlockUnblock = () => {
    //     if (selectedUser) {
    //         const apiCall = selectedUser.blocked ? api.unblockAccount : api.blockAccount;
    //         apiCall(selectedUser.id).then(() => {
    //             fetchUsers();
    //             setAlertDialogOpen(false);
    //             toast({
    //                 title: t("accountSettings.popUp.changeUserDataOK.title"),
    //                 description: selectedUser.blocked ? t("general.userUnblocked") : t("general.userBlocked")
    //             })
    //         }).catch((error) => {
    //             handleApiError(error);
    //         });
    //     }
    //     setAlertDialogOpen(false);
    // };

    const fetchParking = (page?: number) => {
        const actualPage = page != undefined ? page : currentPage;
        const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;

        api.getParking(details)
            .then(response => {
                if (response.status === 200) {
                    setCurrentPage(actualPage);
                    setParking(response.data);
                }
            })
            .catch(error => {
                handleApiError(error);
            });
    };


    useEffect(() => {
        fetchParking();
        setIsSubmitClicked(false);
    }, [isSubmitClicked]);

    function refresh() {
        setIsRefreshing(true);
        fetchParking();
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
                            <BreadcrumbLink>{t("breadcrumb.manageParking")}</BreadcrumbLink>
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
                                className="text-center">{t("City")}</TableHead>
                            <TableHead
                                className="text-center">{t("Street")}</TableHead>
                            <TableHead
                                className="text-center">{t("Zip code")}</TableHead>
                            <TableHead
                                className="text-center">{t("Sector determination strategy")}</TableHead>
                            <TableHead
                                className="text-center">{t("Sector types")}</TableHead>
                            <TableHead className="text-center"></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {parking.map(parking => (
                            <TableRow key={parking.id} className="flex-auto">
                                <TableCell>{parking.city}</TableCell>
                                <TableCell>{parking.street}</TableCell>
                                <TableCell>{parking.zipCode}</TableCell>
                                <TableCell><Badge variant={"default"}>{parking.strategy} </Badge></TableCell>
                                <TableCell>
                                    {parking.sectorTypes.map(level => {
                                        return <Badge key={level} variant={"secondary"}>{level} </Badge>;
                                    })}
                                </TableCell>
                                {/*<TableCell>*/}
                                {/*    <DropdownMenu>*/}
                                {/*        <DropdownMenuTrigger>*/}
                                {/*            <FiSettings/>*/}
                                {/*        </DropdownMenuTrigger>*/}
                                {/*        <DropdownMenuContent>*/}
                                {/*            <DropdownMenuItem onClick={() => handleSettingsClick(user.id)}>*/}
                                {/*                {t("accountSettings.users.table.settings.manage")}*/}
                                {/*            </DropdownMenuItem>*/}
                                {/*            <DropdownMenuItem*/}
                                {/*                onClick={() => handleBlockUnblockClick(user)}*/}
                                {/*                disabled={user.id === account?.id}*/}
                                {/*            >*/}
                                {/*                {user.blocked ? t("accountSettings.users.table.settings.unblock") : t("accountSettings.users.table.settings.block")}*/}
                                {/*            </DropdownMenuItem>*/}
                                {/*        </DropdownMenuContent>*/}
                                {/*    </DropdownMenu>*/}
                                {/*</TableCell>*/}
                            </TableRow>
                        ))}
                    </TableBody>
                    {/*<AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>*/}
                    {/*    <AlertDialogContent>*/}
                    {/*        <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>*/}
                    {/*        <AlertDialogDescription>*/}
                    {/*            {t("accountSettings.users.table.settings.block.confirm1")}*/}
                    {/*            {selectedUser?.blocked ? t("accountSettings.users.table.settings.unblock2") : t("accountSettings.users.table.settings.block2")}*/}
                    {/*            {t("accountSettings.users.table.settings.block.confirm2")}*/}
                    {/*        </AlertDialogDescription>*/}
                    {/*        <AlertDialogAction onClick={handleConfirmBlockUnblock}>{t("general.ok")}</AlertDialogAction>*/}
                    {/*        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>*/}
                    {/*    </AlertDialogContent>*/}
                    {/*</AlertDialog>*/}
                </Table>
                <div className={"pt-5"}>
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious
                                    onClick={() => {
                                        if (currentPage > 0) fetchParking(currentPage-1)
                                    }}
                                />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink>{currentPage + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationNext
                                    onClick={() => {
                                        if (parking.length === pageSize) fetchParking(currentPage+1)
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

export default ParkingManagementPage;
