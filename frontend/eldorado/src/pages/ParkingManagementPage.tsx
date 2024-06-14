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
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {Badge} from "@/components/ui/badge.tsx";
import {Loader2, Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {ParkingListType} from "@/types/Parking.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import CreateParkingForm from "@/components/forms/CreateParkingForm.tsx";
import {FiSettings} from "react-icons/fi";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import EditParkingForm from "@/components/forms/EditParkingForm.tsx";
import {useNavigate} from "react-router-dom";
import {Pathnames} from "@/router/pathnames.ts";

function ParkingManagementPage() {
    const [currentPage, setCurrentPage] = useState(0);
    const [parking, setParking] = useState<ParkingListType[]>([]);
    const [isCreateDialogOpen, setCreateDialogOpen] = useState(false);
    const [isEditDialogOpen, setEditDialogOpen] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [parkingId, setParkingId] = useState("");
    const {t} = useTranslation();
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [isSubmitClicked, setIsSubmitClicked] = useState(false);
    const navigate = useNavigate();
    const pageSize = 4;

    const handleDeleteParkingClick = (parkingId: string) => {
        setParkingId(parkingId);
        setAlertDialogOpen(true);
    };

    const handleEditParkingClick = (parkingId: string) => {
        setParkingId(parkingId);
        setEditDialogOpen(true);
    };

    const handleDeleteParking = () => {
        api.deleteParking(parkingId)
            .then(() =>{
                fetchParking();
                setAlertDialogOpen(false);
            })
            .catch(error => {
                handleApiError(error);
            });
    };

    const fetchParking = (page?: number) => {
        const actualPage = page != undefined ? page : currentPage;
        const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;

        api.getParking(details)
            .then(response => {
                if (response.status === 200) {
                    setCurrentPage(actualPage);
                    setParking(response.data);
                } else if (response.status === 204 && actualPage > 0) {
                    fetchParking(actualPage -1)
                } else if (response.status === 204 && actualPage <= 0) {
                    setCurrentPage(0);
                    setParking([]);
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

    const refresh = () => {
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
                            <BreadcrumbLink className="cursor-pointer" onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
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
            <div className="flex justify-start pt-2.5">
                <Dialog open={isCreateDialogOpen} onOpenChange={setCreateDialogOpen}>
                    <DialogTrigger asChild>
                        <Button variant="default">Create Parking</Button>
                    </DialogTrigger>
                    <DialogContent className="sm:max-w-[425px]">
                        <DialogHeader>
                            <DialogTitle>Create Parking</DialogTitle>
                        </DialogHeader>
                        <CreateParkingForm setDialogOpen={setCreateDialogOpen} refresh={refresh}/>
                    </DialogContent>
                </Dialog>
            </div>
            <div className={"pt-1"}>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("parking.management.page.city")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.page.street")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.page.zip.code")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.page.sector.determination.strategy")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.page.sector.types")}</TableHead>
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
                                <TableCell>
                                    <DropdownMenu>
                                        <DropdownMenuTrigger>
                                            <FiSettings/>
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent>
                                            <DropdownMenuItem
                                                onClick={() => navigate(`/manage-parking/${parking.id}`)}
                                            >
                                                {t("parking.management.page.show.sectors")}
                                            </DropdownMenuItem>
                                            <DropdownMenuItem
                                                onClick={() => handleEditParkingClick(parking.id)}
                                            >
                                                {t("parking.management.page.edit.parking")}
                                            </DropdownMenuItem>
                                            <DropdownMenuItem
                                                onClick={() => handleDeleteParkingClick(parking.id)}
                                                disabled={parking.sectorTypes.length !== 0}
                                            >
                                                {t("parking.management.page.delete.parking")}
                                            </DropdownMenuItem>
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                    <Dialog open={isEditDialogOpen} onOpenChange={setEditDialogOpen}>
                        <DialogContent className="sm:max-w-[425px]">
                            <DialogHeader>
                                <DialogTitle>{t("parking.management.page.edit.parking")}</DialogTitle>
                            </DialogHeader>
                            <EditParkingForm setDialogOpen={setEditDialogOpen} refresh={refresh} parkingId={parkingId}/>
                        </DialogContent>
                    </Dialog>
                    <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                        <AlertDialogContent>
                            <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                            <AlertDialogDescription>
                                {t("parking.management.page.are.you.sure.you.want.to.delete.this.parking")}
                            </AlertDialogDescription>
                            <AlertDialogAction onClick={handleDeleteParking}>{t("general.ok")}</AlertDialogAction>
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