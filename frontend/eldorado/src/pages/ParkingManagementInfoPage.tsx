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
import {ParkingType, sectorDTOtoSectorListType, SectorListType, sectorStrategy} from "@/types/Parking.ts";
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
    AlertDialogDescription, AlertDialogFooter, AlertDialogHeader,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import EditSectorForm from "@/components/forms/EditSectorForm.tsx";
import {FiSettings} from "react-icons/fi";
import {DeactivateSectorForm} from "@/components/forms/DeactivateSectorForm.tsx";
export function ParkingManagementInfoPage() {
    const [currentPage, setCurrentPage] = useState(0);
    const [sectors, setSectors] = useState<SectorListType[]>([]);
    const [parking, setParking] = useState<ParkingType>({parkingId:"", version:"", city:"", street:"", zipCode:"", strategy:sectorStrategy.LEAST_OCCUPIED, signature:""})
    const [isCreateDialogOpen, setCreateDialogOpen] = useState(false);
    const [isEditDialogOpen, setEditDialogOpen] = useState(false);
    const [isDeactivateDialogOpen, setDeactivateDialogOpen] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [isActivateDialogOpen, setActivateDialogOpen] = useState(false);
    const [sectorId, setSectorId] = useState("");
    const [sectorDeactivate, setSectorDeactivate] = useState<Date>(new Date());
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

    const handleEditSectorClick = (sectorId: string, date:string) => {
        setSectorId(sectorId);

        //hack 👌👌👌👌👌
        if ( date !== "" ) {
            const temp = date.split('.');
            const temp2 = temp[2].split(' ');
            const tempDate = temp2[0].concat('-').concat(temp[1]).concat('-').concat(temp[0]).concat('T').concat(temp2[1]).concat(":00Z");
            const temp3 = new Date(tempDate);
            temp3.setMinutes(temp3.getMinutes() + (new Date()).getTimezoneOffset())
            setSectorDeactivate(temp3);
        } else {
            setSectorDeactivate(new Date("2099-01-01T00:00:00.000Z"));
        }
        setEditDialogOpen(true);
    };

    const handleDeactivateSectorClick = (sectorId: string) => {
        setSectorId(sectorId);
        setDeactivateDialogOpen(true);
    };

    const handleActivateSectorClick = (sectorId: string) => {
        setSectorId(sectorId);
        setActivateDialogOpen(true);
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

    const handleActivateSector = () => {
        api.activateSector(sectorId)
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
                    // ts-expext-error foff
                    const sectorsTemp:any[] = response.data;
                    const sectorsCopy:SectorListType[] = [];
                    sectorsTemp.forEach((s) => {sectorsCopy.push(sectorDTOtoSectorListType(s))})
                    setSectors(sectorsCopy);
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
                                className="text-center">{t("parking.management.info.page.city")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.info.page.street")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.info.page.zip.code")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.info.page.strategy")}</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        <TableRow key={parking.parkingId} className="flex-auto">
                            <TableCell>{parking.city}</TableCell>
                            <TableCell>{parking.street}</TableCell>
                            <TableCell>{parking.zipCode}</TableCell>
                            <TableCell><Badge variant={"default"}>{t(parking.strategy)} </Badge></TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </div>
            <div className="flex justify-start pt-2.5">
                <Dialog open={isCreateDialogOpen} onOpenChange={setCreateDialogOpen}>
                    <DialogTrigger asChild>
                        <Button variant="default">{t("parking.management.info.page.create.sector")}</Button>
                    </DialogTrigger>
                    <DialogContent className="sm:max-w-[425px]">
                        <DialogHeader>
                            <DialogTitle>{t("parking.management.info.page.create.sector")}</DialogTitle>
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
                                className="text-center">{t("parking.management.info.page.name")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.info.page.deactivation.time")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.info.page.occupies.pleces")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.info.page.max.pleces")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.info.page.type")}</TableHead>
                            <TableHead
                                className="text-center">{t("parking.management.info.page.weight")}</TableHead>
                            <TableHead className="text-center"></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {sectors.map(sector => (
                            <TableRow key={sector.id} className="flex-auto">
                                <TableCell>{sector.name}</TableCell>
                                <TableCell>{sector.deactivationTime}</TableCell>
                                <TableCell>{sector.occupiedPlaces}</TableCell>
                                <TableCell>{sector.maxPlaces}</TableCell>
                                <TableCell><Badge variant={"default"}>{t(sector.type)} </Badge></TableCell>
                                <TableCell>{sector.weight}</TableCell>
                                <TableCell>
                                    <DropdownMenu>
                                        <DropdownMenuTrigger>
                                            <FiSettings/>
                                        </DropdownMenuTrigger>
                                        <DropdownMenuContent>
                                            {sector.deactivationTime !== "" && <DropdownMenuItem
                                                onClick={() => handleActivateSectorClick(sector.id)}>
                                                {t("parking.management.info.page.activate.sector")}
                                            </DropdownMenuItem>}
                                            {sector.deactivationTime === "" && <DropdownMenuItem
                                                onClick={() => handleDeactivateSectorClick(sector.id)}>
                                                {t("parking.management.info.page.deactivate.sector")}
                                            </DropdownMenuItem>}
                                            <DropdownMenuItem
                                                onClick={() => handleEditSectorClick(sector.id, sector.deactivationTime)}>
                                                {t("parking.management.info.page.edit.sector")}
                                            </DropdownMenuItem>
                                            <DropdownMenuItem
                                                onClick={() => handleDeleteParkingClick(sector.id)}>
                                                {t("parking.management.info.page.delete.sector")}
                                            </DropdownMenuItem>
                                        </DropdownMenuContent>
                                    </DropdownMenu>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                    <AlertDialog open={isDeactivateDialogOpen} onOpenChange={setDeactivateDialogOpen}>
                        <AlertDialogContent className="sm:max-w-[425px]">
                            <AlertDialogHeader>
                                <AlertDialogTitle>{t("parking.management.info.page.deactivate.parking")}</AlertDialogTitle>
                            </AlertDialogHeader>
                            <DeactivateSectorForm setDialogOpen={setDeactivateDialogOpen} refresh={refresh} sectorId={sectorId}/>
                        </AlertDialogContent>
                    </AlertDialog>
                    <Dialog open={isEditDialogOpen} onOpenChange={setEditDialogOpen}>
                        <DialogContent className="sm:max-w-[425px]">
                            <DialogHeader>
                                <DialogTitle>{t("parking.management.info.page.edit.parking")}</DialogTitle>
                            </DialogHeader>
                            <EditSectorForm setDialogOpen={setEditDialogOpen} refresh={refresh} sectorId={sectorId} sectorDeactivate={sectorDeactivate}/>
                        </DialogContent>
                    </Dialog>
                    <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                        <AlertDialogContent>
                            <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                            <AlertDialogDescription>
                                {t("parking.management.info.page.are.you.sure.you.want.to.delete.this.sector")}
                            </AlertDialogDescription>
                            <AlertDialogFooter>
                                <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                <AlertDialogAction onClick={handleDeleteSector}>{t("general.ok")}</AlertDialogAction>
                            </AlertDialogFooter>
                        </AlertDialogContent>
                    </AlertDialog>
                    <AlertDialog open={isActivateDialogOpen} onOpenChange={setActivateDialogOpen}>
                        <AlertDialogContent>
                            <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                            <AlertDialogDescription>
                                {t("parking.management.info.page.are.you.sure.you.want.to.activate.this.sector")}
                            </AlertDialogDescription>
                            <AlertDialogFooter>
                                <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                                <AlertDialogAction onClick={handleActivateSector}>{t("general.ok")}</AlertDialogAction>
                            </AlertDialogFooter>
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