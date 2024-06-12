import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {Dispatch, SetStateAction, useEffect, useState} from "react";
import {Loader2} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {SectorType, SectorTypes} from "@/types/Parking.ts";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";

type editSectorFormProps = {
    setDialogOpen: Dispatch<SetStateAction<boolean>>
    refresh: () => void
    sectorId: string
}

function EditParkingForm({setDialogOpen, refresh, sectorId}:editSectorFormProps) {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [editedSector, setEditedSector] = useState<SectorType>({id:"", parkingId:"", version:"", name:"", type:SectorTypes.UNCOVERED, maxPlaces:0, weight:0, signature:""})
    const [sector, setSector] = useState<SectorType>({id:"", parkingId:"", version:"", name:"", type:SectorTypes.UNCOVERED, maxPlaces:0, weight:0, signature:""});
    const formSchema = z.object({
        maxPlaces: z.coerce.number({message: "ZMIEN"}).int({message: "ZMIEN"}).min(0, {message: "ZMIEN"}).max(1000,{message: "ZMIEN"}),
        type: z.enum(["UNCOVERED", "COVERED", "UNDERGROUND"]),
        weight: z.coerce.number({message: "ZMIEN"}).int({message: "ZMIEN"}).min(1, {message: "ZMIEN"}).max(100,{message: "ZMIEN"})
    })

    useEffect(() => {
        api.getSectorById(sectorId)
            .then((response) => {
                setSector({...response.data, signature: response.headers['etag']})
            })
            .catch(error => {
                handleApiError(error);
            });
    }, []);


    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            maxPlaces: sector.maxPlaces,
            type: sector.type,
            weight: sector.weight
        },
    })

    useEffect(() => {
        form.reset({
            maxPlaces: sector.maxPlaces,
            type: sector.type,
            weight: sector.weight
        });
    }, [sector]);

    async function handleAlertDialog(){
        api.modifySector(editedSector)
            .then(() => {
                refresh();
                setAlertDialogOpen(false);
                setDialogOpen(false)})
            .catch(error => {
                setAlertDialogOpen(false);
                handleApiError(error);
            });
    }

    async function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true);
        try {
            setEditedSector({
                id: sector.id,
                parkingId: sector.parkingId,
                version: sector.version,
                name: sector.name,
                type: SectorTypes[values.type],
                maxPlaces: values.maxPlaces,
                weight: values.weight,
                signature: sector.signature
            })
            setAlertDialogOpen(true);
        } catch (error) {
            console.log(error);
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                    control={form.control}
                    name="maxPlaces"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">Max places</FormLabel>
                                <FormControl>
                                    <Input placeholder="1" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="weight"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">Weight</FormLabel>
                                <FormControl>
                                    <Input placeholder="1" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="type"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">Sector Type</FormLabel>
                                <Select onValueChange={field.onChange} value={field.value}>
                                    <FormControl>
                                        <SelectTrigger>
                                            <SelectValue placeholder="Select a sector type." />
                                        </SelectTrigger>
                                    </FormControl>
                                    <SelectContent>
                                        <SelectItem value="UNCOVERED">Uncovered</SelectItem>
                                        <SelectItem value="COVERED">Covered</SelectItem>
                                        <SelectItem value="UNDERGROUND">Underground</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                        </FormItem>
                    )}
                />
                <Button type="submit" className="w-full" disabled={isLoading}>
                    {isLoading ? (
                        <>
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                        </>
                    ) : (
                        t("parkingManagementPage.createParking")
                    )}
                </Button>
            </form>
            <AlertDialog open={isAlertDialogOpen} onOpenChange={setAlertDialogOpen}>
                <AlertDialogContent>
                    <AlertDialogTitle>{t("general.confirm")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        Are you sure you want edit this sector?
                    </AlertDialogDescription>
                    <AlertDialogAction onClick={handleAlertDialog}>
                        {t("general.ok")}
                    </AlertDialogAction>
                    <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                </AlertDialogContent>
            </AlertDialog>
        </Form>
    )
}

export default EditParkingForm
