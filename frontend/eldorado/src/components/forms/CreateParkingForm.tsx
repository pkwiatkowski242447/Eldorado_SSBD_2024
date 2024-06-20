import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {z} from "zod";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormMessage,} from "@/components/ui/form"
import {FormLabel} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {Dispatch, SetStateAction, useState} from "react";
import {Loader2} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select.tsx";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription, AlertDialogFooter,
    AlertDialogTitle
} from "@/components/ui/alert-dialog.tsx";
import {CreateParkingType, sectorStrategy} from "@/types/Parking.ts";
import {api} from "@/api/api.ts";
import handleApiError from "@/components/HandleApiError.ts";

type createParkingFormProps = {
    setDialogOpen: Dispatch<SetStateAction<boolean>>
    refresh: () => void
}

function CreateParkingForm({setDialogOpen, refresh}:createParkingFormProps) {
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);
    const [isAlertDialogOpen, setAlertDialogOpen] = useState(false);
    const [newParking, setNewParking] = useState<CreateParkingType>({city:"", street:"", zipCode:"", strategy:sectorStrategy.LEAST_OCCUPIED})
    const formSchema = z.object({
        city: z.string({message: t("general.field.required")}).min(2, {message: t("create.parking.form.city.too.short")})
            .max(50, {message: t("create.parking.form.city.too.long")}).regex(RegExp("^([a-zA-Z\\u0080-\\u024FąĄćĆęĘłŁńŃóÓśŚźŹżŻ]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024FąĄćĆęĘłŁńŃóÓśŚźŹżŻ]*$"), {message: t("create.parking.form.city.regex.not.met")}),
        street: z.string({message: t("general.field.required")}).min(2, {message: t("create.parking.form.street.too.short")})
            .max(60, {message: t("create.parking.form.street.too.long")}).regex(RegExp("^[A-Za-ząĄćĆęĘłŁńŃóÓśŚźŹżŻ0-9.-]{5,50}$"), {message: t("create.parking.form.street.regex.not.met")}),
        zipCode: z.string({message: t("general.field.required")}).min(6, {message: t("create.parking.form.zip.code.too.short")})
            .max(6, {message: t( "create.parking.form.zip.code.too.long")}).regex(RegExp("^\\d{2}-\\d{3}$"), {message: t("create.parking.form.zip.code.regex.not.met")}),
        strategy: z.enum(["LEAST_OCCUPIED", "MOST_OCCUPIED", "LEAST_OCCUPIED_WEIGHTED"])
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            city: "",
            street: "",
            zipCode: "",
            strategy: "LEAST_OCCUPIED"
        },
    })

    async function handleAlertDialog(){
        api.createParking(newParking)
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
            setNewParking({
                city: values.city,
                street:values.street,
                zipCode:values.zipCode,
                strategy:sectorStrategy[values.strategy]
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
                    name="city"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("create.parking.form.city")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="Warszawa" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="street"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("create.parking.form.street")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="Ziemniaczana" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="zipCode"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("create.parking.form.zip.code")}</FormLabel>
                                <FormControl>
                                    <Input placeholder="12-345" {...field} />
                                </FormControl>
                                <FormMessage/>
                            </div>
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="strategy"
                    render={({field}) => (
                        <FormItem>
                            <div className="grid gap-4">
                                <FormLabel className="text-left">{t("create.parking.form.strategy")}</FormLabel>
                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                    <FormControl>
                                        <SelectTrigger>
                                            <SelectValue placeholder={t("create.parking.form.select.strategy")} />
                                        </SelectTrigger>
                                    </FormControl>
                                    <SelectContent>
                                        <SelectItem value="LEAST_OCCUPIED">{t("create.parking.form.least.occupied")}</SelectItem>
                                        <SelectItem value="MOST_OCCUPIED">{t("create.parking.form.most.occupied")}</SelectItem>
                                        <SelectItem value="LEAST_OCCUPIED_WEIGHTED">{t("create.parking.form.least.occupied.weighted")}</SelectItem>
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
                        {t("create.parking.form.are.you.sure.you.want.to.create.new.parking")}
                    </AlertDialogDescription>
                    <AlertDialogFooter>
                        <AlertDialogCancel>{t("general.cancel")}</AlertDialogCancel>
                        <AlertDialogAction onClick={handleAlertDialog}>
                            {t("general.ok")}
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </Form>
    )
}

export default CreateParkingForm
