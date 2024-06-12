import {useNavigate, useParams} from "react-router-dom";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Loader2, Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {ParkingType, SectorListType, SectorStrategy} from "@/types/Parking.ts";
import {Pathnames} from "@/router/pathnames.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {Badge} from "@/components/ui/badge.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink, PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu.tsx";
import {Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger} from "@/components/ui/dialog.tsx";
import CreateSectorForm from "@/components/forms/CreateSectorForm.tsx";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import EditSectorForm from "@/components/forms/EditSectorForm.tsx";
import {FiSettings} from "react-icons/fi";
import {DateTimePickerForm} from "@/components/forms/date-time-picker-form.tsx";
import {DeactivateSectorForm} from "@/components/forms/DeactivateSectorForm.tsx";
export function ParkingManagementInfoPage() {
    const [currentPage, setCurrentPage] = useState(0);
    const [sectors, setSectors] = useState<SectorListType[]>([]);
    const [parking, setParking] = useState<ParkingType>({parkingId:"", version:"", city:"", street:"", zipCode:"", strategy:SectorStrategy.LEAST_OCCUPIED, signature:""})
    const [isCreateDialogOpen, setCreateDialogOpen] = useState(false);
    const [isEditDialogOpen, setEditDialogOpen] = useState(false);
    const [isDeactivateDialogOpen, setDeactivateDialogOpen] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [sectorId, setSectorId] = useState("");
    const {t} = useTranslation();
    const {id} = useParams<{ id: string }>();
    const [isRefreshing, setIsRefreshing] = useState(false);
    const [isSubmitClicked, setIsSubmitClicked] = useState(false);
    const navigate = useNavigate();
    const pageSize = 4;
    // const [date, setDate] = useState<Date>();

    const handleDeleteParkingClick = (sectorId: string) => {
        setSectorId(sectorId);
        setAlertDialogOpen(true);
    };

    const handleEditSectorClick = (sectorId: string) => {
        setSectorId(sectorId);
        setEditDialogOpen(true);
    };

    const handleDeactivateSectorClick = (sectorId: string) => {
        setSectorId(sectorId);
        setDeactivateDialogOpen(true);
    };

    const handleDeleteSector = () => {
        api.deleteSector(sectorId)
            .then(() =>{
                fetchSectors();
                setAlertDialogOpen(false);
            })
            .catch(error => {
                handleApiError(error);
            });
    };

    const fetchSectors = (page?: number) => {
        const actualPage = page != undefined ? page : currentPage;
        const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;

        api.getSectorsStaff(id, details)
            .then(response => {
                if (response.status === 200) {
                    setCurrentPage(actualPage);
                    setSectors(response.data);
                } else if (response.status === 204 && actualPage > 0) {
                    fetchSectors(actualPage -1)
                } else if (response.status === 204 && actualPage <= 0) {
                    setCurrentPage(0);
                    setSectors([]);
                }
            })
            .catch(error => {
                handleApiError(error);
            });
    };

    const fetchParking = () => {
        api.getParkingById(id ? id : "0")
            .then((response) => {
                setParking({...response.data, signature: response.headers['etag']})
            })
            .catch(error => {
                handleApiError(error);
            });
    }


    useEffect(() => {
        fetchParking();
        fetchSectors();
        setIsSubmitClicked(false);
    }, [isSubmitClicked]);

    const refresh = () => {
        setIsRefreshing(true);
        fetchParking();
        fetchSectors();
        setTimeout(() => setIsRefreshing(false), 1000);
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer"
                                            onClick={() => navigate(Pathnames.public.home)}>{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink className="cursor-pointer"
                                            onClick={() => navigate(Pathnames.staff.parkingManagement)}>{t("breadcrumb.manageParking")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.manageParkingInfo")}</BreadcrumbLink>
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
            <div>
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
                                className="text-center">{t("Strategy")}</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        <TableRow key={parking.parkingId} className="flex-auto">
                            <TableCell>{parking.city}</TableCell>
                            <TableCell>{parking.street}</TableCell>
                            <TableCell>{parking.zipCode}</TableCell>
                            <TableCell><Badge variant={"default"}>{parking.strategy} </Badge></TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </div>
            <div className="flex justify-start pt-2.5">
                <Dialog open={isCreateDialogOpen} onOpenChange={setCreateDialogOpen}>
                    <DialogTrigger asChild>
                        <Button variant="default">Create Sector</Button>
                    </DialogTrigger>
                    <DialogContent className="sm:max-w-[425px]">
                        <DialogHeader>
                            <DialogTitle>Create Sector</DialogTitle>
                        </DialogHeader>
                        <CreateSectorForm setDialogOpen={setCreateDialogOpen} refresh={refresh} parkingId={parking.parkingId}/>
                    </DialogContent>
                </Dialog>
            </div>
            <div className={"pt-1"}>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead
                                className="text-center">{t("Name")}</TableHead>
                            <TableHead
                                className="text-center">{t("Occupies Places")}</TableHead>
                            <TableHead
                                className="text-center">{t("Max Places")}</TableHead>
                            <TableHead
                                className="text-center">{t("Type")}</TableHead>
                            <TableHead
                                className="text-center">{t("Weight")}</TableHead>
                            <TableHead className="text-center"></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {sectors.map(sector => (
                            <TableRow key={sector.id} className="flex-auto">
                                <TableCell>{sector.name}</TableCell>
                                <TableCell>{sector.availablePlaces}</TableCell>
                                <TableCell>{sector.maxPlaces}</TableCell>
                                <TableCell><Badge variant={"default"}>{sector.type} </Badge></TableCell>
                                <TableCell>{sector.weight}</TableCell>
                                <TableCell>
                                    <DropdownMenu>
                                        <DropdownMenuTrigger>
                                            <FiSettings/>
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent>
                                            <DropdownMenuItem
                                                onClick={() => handleDeactivateSectorClick(sector.id)}>
                                                Deactivate sector
                                            </DropdownMenuItem>
                                            <DropdownMenuItem
                                                onClick={() => handleEditSectorClick(sector.id)}>
                                                Edit sector
                                            </DropdownMenuItem>
                                            <DropdownMenuItem
                                                onClick={() => handleDeleteParkingClick(sector.id)}>
                                                Delete parking
                                            </DropdownMenuItem>
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                    <Dialog open={isDeactivateDialogOpen} onOpenChange={setDeactivateDialogOpen}>
                        <DialogContent className="sm:max-w-[425px]">
                            <DialogHeader>
                                <DialogTitle>Edit Parking</DialogTitle>
                            </DialogHeader>
                            <DeactivateSectorForm setDialogOpen={setDeactivateDialogOpen} refresh={refresh} sectorId={sectorId}/>
                        </DialogContent>
                    </Dialog>
                    <Dialog open={isEditDialogOpen} onOpenChange={setEditDialogOpen}>
                        <DialogContent className="sm:max-w-[425px]">
                            <DialogHeader>
                                <DialogTitle>Edit Parking</DialogTitle>
                            </DialogHeader>
                            <EditSectorForm setDialogOpen={setEditDialogOpen} refresh={refresh} sectorId={sectorId}/>
                        </DialogContent>
                    </Dialog>
                    <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                        <AlertDialogContent>
                            <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                            <AlertDialogDescription>
                                Are you sure you want to delete this sector?
                            </AlertDialogDescription>
                            <AlertDialogAction onClick={handleDeleteSector}>{t("general.ok")}</AlertDialogAction>
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
                                        if (currentPage > 0) fetchSectors(currentPage - 1)
                                    }}
                                />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink>{currentPage + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationNext
                                    onClick={() => {
                                        if (sectors.length === pageSize) fetchSectors(currentPage + 1)
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