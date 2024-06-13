import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Slash} from "lucide-react";
import {Button} from "@/components/ui/button.tsx";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import {ParkingListType} from "@/types/Parking.ts";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table.tsx";
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {Pathnames} from "@/router/pathnames.ts";
import {useNavigate} from "react-router-dom";

function EnterParkingWithoutReservationPage() {
    const [currentPage, setCurrentPage] = useState(0);
    const [parking, setParking] = useState<ParkingListType[]>([]);
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [isResultDialogOpen, setIsResultDialogOpen] = useState(false);
    const [selectedParkingId, setSelectedParkingId] = useState<string | null>(null);
    const [parkingDetails, setParkingDetails] = useState<{ id: string, sectorName: string } | null>(null);
    const pageSize = 5;
    const navigate = useNavigate();

    const fetchParking = (page?: number) => {
        const actualPage = page != undefined ? page : currentPage;
        const details = `?pageNumber=${actualPage}&pageSize=${pageSize}`;

        api.getActiveParking(details)
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
    }, [currentPage, pageSize]);

    const handleEnterParking = async () => {
        if (selectedParkingId) {
            try {
                const response = await api.enterParkingWithoutReservation(selectedParkingId);
                setParkingDetails({
                    id: response.data.id,
                    sectorName: response.data.sectorName
                });
                setIsDialogOpen(false);
                setIsResultDialogOpen(true);
            } catch (error) {
                // @ts-expect-error ignore this for now
                handleApiError(error);
            }
        }
    };

    const t = useTranslation().t;

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
                            <BreadcrumbLink>{"Enter Parking"}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <div className="mx-auto grid w-full max-w-6xl gap-2 p-10">
                <h1 className="text-3xl font-semibold">Enter Parking</h1>
            </div>
            <div className={"pt-5"}>
                <Table className="p-10 flex-grow">
                    <TableHeader>
                        <TableRow className={"text-center p-10"}>
                            <TableHead className="text-center">{t("City")}</TableHead>
                            <TableHead className="text-center">{t("Street")}</TableHead>
                            <TableHead className="text-center">{t("Zip code")}</TableHead>
                            <TableHead className="text-center"></TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody className={"text-center"}>
                        {parking.map(parking => (
                            <TableRow key={parking.id} className="flex-auto">
                                <TableCell>{parking.city}</TableCell>
                                <TableCell>{parking.street}</TableCell>
                                <TableCell>{parking.zipCode}</TableCell>
                                <TableCell>
                                    <Button variant={"default"} onClick={() => {
                                        setSelectedParkingId(parking.id);
                                        setIsDialogOpen(true);
                                    }}>{"Enter"}</Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                <div className={"pt-5"}>
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious
                                    onClick={() => {
                                        if (currentPage > 0) fetchParking(currentPage - 1);
                                    }}
                                />
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationLink>{currentPage + 1}</PaginationLink>
                            </PaginationItem>
                            <PaginationItem>
                                <PaginationNext
                                    onClick={() => {
                                        if (parking.length === pageSize) fetchParking(currentPage + 1);
                                    }}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                </div>
            </div>
            <AlertDialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>Enter Parking</AlertDialogTitle>
                        <AlertDialogDescription>
                            Are you sure you want to park here?
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogCancel onClick={() => setIsDialogOpen(false)}>No</AlertDialogCancel>
                        <AlertDialogAction onClick={handleEnterParking}>Yes</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
            <AlertDialog open={isResultDialogOpen} onOpenChange={setIsResultDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>You have entered the parking. Write down the information provided
                            below.</AlertDialogTitle>
                        <AlertDialogDescription>
                            {parkingDetails && (
                                <>
                                    <p><strong>ID:</strong> {parkingDetails.id}</p>
                                    <p><strong>Sector Name:</strong> {parkingDetails.sectorName}</p>
                                </>
                            )}
                        </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                        <AlertDialogAction onClick={() => setIsResultDialogOpen(false)}>OK</AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </div>
    );
}

export default EnterParkingWithoutReservationPage;
